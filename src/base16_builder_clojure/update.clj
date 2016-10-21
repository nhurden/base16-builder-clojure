(ns base16-builder-clojure.update
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as file]
            [me.raynes.conch :refer [with-programs]]))

(defn load-yaml-file [filename]
  (-> filename
      slurp
      yaml/parse-string))

(defn file-exists [filename]
  (.exists (file/as-file filename)))

(defn git-pull [dir]
  (with-programs [git]
    (print (git "-C" dir "pull"))))

(defn git-clone [url dir]
  (with-programs [git]
    (print (git "clone" url dir))))

(defn update-repo-map
  "Fetches the repositories in `m` with keys specifying directory names and
  values representing repository URLs into the directory specified by `base-path`."
  [m base-path]
  (doseq [[repo-name url] m]
    (let [dir (str base-path "/" (name repo-name))]
      (println "Fetching" dir)
      (if (file-exists dir)
        (git-pull dir)
        (git-clone url dir)))))

(defn update-source-lists []
  (println "Updating Sources:")
  (-> (load-yaml-file "sources.yaml")
      (update-repo-map "sources")))

(defn update-schemes []
  (println "Updating Schemes:")
  (-> (load-yaml-file "sources/schemes/list.yaml")
      (update-repo-map "schemes")))

(defn update-templates []
  (println "Updating Templates:")
  (-> (load-yaml-file "sources/templates/list.yaml")
      (update-repo-map "templates")))

(defn update-all []
  (update-source-lists)
  (update-schemes)
  (update-templates))

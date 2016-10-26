(ns base16-builder-clojure.update
  (:require [base16-builder-clojure.io :refer [load-yaml-file ->path print-header]]
            [me.raynes
             [conch :refer [with-programs]]
             [fs :as fs]]))

(defn git-pull [dir]
  (println "Pulling" dir)
  (with-programs [git]
    (println (git "-C" dir "pull"))))

(defn git-clone [url dir]
  (println "Cloning" url "into" dir)
  (with-programs [git]
    (println (git "clone" url dir))))

(defn fetch-repository [repo-name url into-dir]
  (let [dir (->path into-dir repo-name)]
    (if (fs/exists? dir)
      (git-pull dir)
      (git-clone url dir))))

(defn update-repo-map
  "Fetches the repositories in `m` with keys specifying repository names and
  values representing repository URLs into the directory specified by `base-path`."
  [m base-path]
  (doseq [[repo-name url] m]
    (fetch-repository (name repo-name) url base-path)))

(defn update-source-lists []
  (print-header "Updating Sources:")
  (-> (load-yaml-file "sources.yaml")
      (update-repo-map "sources")))

(defn update-schemes []
  (print-header "Updating Schemes:")
  (-> (load-yaml-file "sources/schemes/list.yaml")
      (update-repo-map "schemes")))

(defn update-templates []
  (print-header "Updating Templates:")
  (-> (load-yaml-file "sources/templates/list.yaml")
      (update-repo-map "templates")))

(defn update-all []
  (update-source-lists)
  (update-schemes)
  (update-templates))

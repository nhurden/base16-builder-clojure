(ns base16-builder-clojure.io
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [me.raynes.fs :as fs]))

(defn ->path [& components]
  (str/join "/" components))

(defn load-yaml-file [filename]
  (-> filename
      slurp
      yaml/parse-string))

(defn scheme-names []
  (->> (load-yaml-file "sources/schemes/list.yaml")
       keys
       (map name)))

(defn template-names []
  (->> (load-yaml-file "sources/templates/list.yaml")
       keys
       (map name)))

(defn template-configs [name]
  (-> (->path "templates" name "templates" "config.yaml")
      load-yaml-file))

(defn scheme-files [name]
  (->> (->path "schemes" name "*.yaml")
       fs/glob
       (map load-yaml-file)))

(defn remove-existing-output [path extension]
  (let [files (fs/glob (str path "/base16-*" extension))]
    (doseq [file files] (io/delete-file file))))

(defn output-path [name config]
  (->path "templates" name (:output config)))

(defn print-header [s]
  (println)
  (println "---")
  (println s)
  (println "---")
  (println))

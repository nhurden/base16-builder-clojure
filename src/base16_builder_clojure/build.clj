(ns base16-builder-clojure.build
  (:require [base16-builder-clojure.util :refer [load-yaml-file]]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [clostache.parser :refer [render-resource]]
            [slugger.core :refer [->slug]]))

(defn scheme-names []
  (->> (load-yaml-file "sources/schemes/list.yaml")
       keys
       (map name)))

(defn template-names []
  (->> (load-yaml-file "sources/templates/list.yaml")
       keys
       (map name)))

(defn template-configs [name]
  (-> (str "templates/" name "/templates/config.yaml")
      load-yaml-file))

(defn scheme-files [name]
  (->> (str "schemes/" name "/*.yaml")
       fs/glob
       (map load-yaml-file)))

(defn remove-existing-output [path extension]
  (let [files (fs/glob (str path "/base16-*" extension))]
    (doseq [file files] (io/delete-file file))))

(defn output-path [config]
  (str "templates/" name "/" (:output config)))

(defn print-header [s]
  (println)
  (println "---")
  (println s)
  (println "---")
  (println))

(defn assoc-bases [m scheme-desc] m)

(defn template-data [scheme-desc]
  (-> {:scheme-slug (->slug (:scheme scheme-desc))
       :scheme-name (:scheme scheme-desc)
       :scheme-author (:author scheme-desc)}
      (assoc-bases scheme-desc)))

(defn build-scheme
  "Build a single template/scheme pair"
  [name template-filename config scheme]
  (doseq [scheme-desc (scheme-files scheme)]
    (let [data (template-data scheme-desc)
          slug (:scheme-slug data)
          extension (:extension config)
          output-filename (str "base16-" slug extension)]
      (println "Building" output-filename))))

(defn build-template-config
  "Build one config of a template for all given schemes"
  [template-name filename config schemes]
  (print-header (str "Building " template-name " – " (name filename)))
  (remove-existing-output (output-path config) (:extension config))
  (doseq [scheme schemes]
    (build-scheme template-name filename config scheme)))

(defn build-template
  "Build all configs for a given template"
  ([name]
   (let [schemes (scheme-names)]
     (when (empty? schemes)
       (println "No schemes found - did you run update?"))
     (build-template name schemes)))

  ([name schemes]
   (doseq [[file config] (template-configs name)]
     (build-template-config name file config schemes))))

(defn build-all []
  (let [templates (template-names)]
    (when (empty? templates)
      (println "No templates found - did you run update?"))
    (doseq [template templates]
      (build-template template))))

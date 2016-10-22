(ns base16-builder-clojure.build
  (:require [base16-builder-clojure
             [io :refer :all]
             [scheme :refer [scheme-data]]]
            [clostache.parser :refer [render]]
            [me.raynes.fs :as fs]))

(defn build-scheme
  "Build a single template/scheme pair"
  [template-name template-filename config scheme]
  (doseq [scheme-desc (scheme-files scheme)]
    (let [data (scheme-data scheme-desc)
          slug (:scheme-slug data)
          extension (:extension config)
          out-filename (str "base16-" slug extension)
          out-path (output-path template-name config)
          out-target (->path out-path out-filename)
          template-path (->path "templates" template-name "templates" (str (name template-filename) ".mustache"))
          template (slurp template-path)
          rendered (render template data)
          ]
      (println "Building" out-filename)
      (when (not (fs/exists? out-path))
        (fs/mkdir out-path))
      (spit out-target rendered))))

(defn build-template-config
  "Build one config of a template for all given schemes"
  [template-name filename config schemes]
  (print-header (str "Building " template-name " – " (name filename)))
  (remove-existing-output (output-path template-name config) (:extension config))
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

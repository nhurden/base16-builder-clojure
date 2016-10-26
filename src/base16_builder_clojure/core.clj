(ns base16-builder-clojure.core
  (:gen-class)
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [base16-builder-clojure.build :refer [build-all build-template]]
            [base16-builder-clojure.update :refer [update-all fetch-repository]]
            [base16-builder-clojure.io :refer [scheme-names template-names]]))

 (def cli-options
   [[nil "--template-name NAME" "Name of the template repository to build"
     :default nil]
    [nil "--template-dir DIRECTORY" "Directory of the template repository to build"
     :default nil]
    ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Usage: base16-builder-clojure [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  build      Build all schemes for all templates"
        "  update     Update schemes and templates"
        "  templates  List the available templates"
        "  schemes    List the available schemes"]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn list-templates []
  (apply println (template-names)))

(defn list-schemes []
  (apply println (scheme-names)))

(defn build-single [name dir]
  (fetch-repository name dir "templates")
  (build-template name))

(defn build [name dir]
  (if (and name dir)
    (build-single name dir)
    (build-all)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))

    ;; Execute program with options
    (case (first arguments)
      "build" (build (:template-name options) (:template-dir options))
      "update" (update-all)
      "templates" (list-templates)
      "schemes" (list-schemes)
      (exit 1 (usage summary)))))

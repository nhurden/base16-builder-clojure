(ns base16-builder-clojure.core
  (:gen-class)
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [base16-builder-clojure.build :refer [build-all]]
            [base16-builder-clojure.update :refer [update-all]]))

 (def cli-options
   [["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Usage: base16-builder-clojure [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  build    Build all schemes for all templates"
        "  update   Update schemes and templates"]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))

    ;; Execute program with options
    (case (first arguments)
      "build" (build-all)
      "update" (update-all)
      (exit 1 (usage summary)))))

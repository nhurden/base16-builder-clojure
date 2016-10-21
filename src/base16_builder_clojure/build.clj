(ns base16-builder-clojure.build
  (:require [base16-builder-clojure.util :refer [load-yaml-file]]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [clostache.parser :refer [render]]
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

(defn output-path [name config]
  (str "templates/" name "/" (:output config)))

(defn print-header [s]
  (println)
  (println "---")
  (println s)
  (println "---")
  (println))

(def color-keys
  [:base00 :base01 :base02 :base03 :base04 :base05 :base06 :base07
   :base08 :base09 :base0A :base0B :base0C :base0D :base0E :base0F])

(defn hex-components [color]
  [(subs color 0 2)
   (subs color 2 4)
   (subs color 4 6)])

(defn rgb-components [color]
  (defn hex->dec [hex]
    (str (read-string (str "0x" hex))))

  (map hex->dec (hex-components color)))

(defn color-map [scheme-desc key]
  (let [color (key scheme-desc)
        [hex-r hex-g hex-b] (hex-components color)
        [rgb-r rgb-g rgb-b] (rgb-components color)]
    (defn k [suffix] (keyword (str (name key) suffix)))
    {(k "-hex") color
     (k "-hex-r") hex-r
     (k "-hex-g") hex-g
     (k "-hex-b") hex-b
     (k "-rgb-r") rgb-r
     (k "-rgb-g") rgb-g
     (k "-rgb-b") rgb-b}))

(defn assoc-bases [m scheme-desc]
  (merge m (apply merge (map #(color-map scheme-desc %) color-keys))))

(defn template-data [scheme-desc]
  (-> {:scheme-slug (->slug (:scheme scheme-desc))
       :scheme-name (:scheme scheme-desc)
       :scheme-author (:author scheme-desc)}
      (assoc-bases scheme-desc)))

(defn build-scheme
  "Build a single template/scheme pair"
  [template-name template-filename config scheme]
  (doseq [scheme-desc (scheme-files scheme)]
    (let [data (template-data scheme-desc)
          slug (:scheme-slug data)
          extension (:extension config)
          out-filename (str "base16-" slug extension)
          out-path (output-path template-name config)
          out-target (str out-path "/" out-filename)
          template-path (str "templates/" template-name "/templates/" (name template-filename) ".mustache")
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

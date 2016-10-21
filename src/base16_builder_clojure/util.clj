(ns base16-builder-clojure.util
  (:require [clj-yaml.core :as yaml]))

(defn load-yaml-file [filename]
  (-> filename
      slurp
      yaml/parse-string))

(ns base16-builder-clojure.scheme
  (:require [slugger.core :refer [->slug]]))

(def color-keys
  [:base00 :base01 :base02 :base03 :base04 :base05 :base06 :base07
   :base08 :base09 :base0A :base0B :base0C :base0D :base0E :base0F])

(defn hex-components [color]
  [(subs color 0 2)
   (subs color 2 4)
   (subs color 4 6)])

(defn rgb-components [color]
  (defn hex->dec [hex]
    (read-string (str "0x" hex)))

  (map hex->dec (hex-components color)))

(defn color-map [scheme-desc key]
  (let [color (key scheme-desc)
        [hex-r hex-g hex-b] (hex-components color)
        [rgb-r rgb-g rgb-b] (rgb-components color)]
    (defn k [suffix] (keyword (str (name key) suffix)))
    (defn float-color [color]
      (str (float (/ color 255))))
    {(k "-hex") color
     (k "-hex-r") hex-r
     (k "-hex-g") hex-g
     (k "-hex-b") hex-b
     (k "-rgb-r") (str rgb-r)
     (k "-rgb-g") (str rgb-g)
     (k "-rgb-b") (str rgb-b)
     (k "-rgbf-r") (float-color rgb-r)
     (k "-rgbf-g") (float-color rgb-g)
     (k "-rgbf-b") (float-color rgb-b)}))

(defn assoc-bases [m scheme-desc]
  (merge m (apply merge (map #(color-map scheme-desc %) color-keys))))

(defn scheme-data [scheme-desc]
  (-> {:scheme-slug (->slug (:scheme scheme-desc))
       :scheme-name (:scheme scheme-desc)
       :scheme-author (:author scheme-desc)}
      (assoc-bases scheme-desc)))

(ns base16-builder-clojure.scheme-test
  (:require [base16-builder-clojure.scheme :as sut]
            [clojure.test :as t :refer :all]))

(def scheme-desc
  {:author "Chris Kempson (http://chriskempson.com)"
   :base00 "f8f8f8"
   :base01 "e8e8e8"
   :base02 "d8d8d8"
   :base03 "b8b8b8"
   :base04 "585858"
   :base05 "383838"
   :base06 "282828"
   :base07 "181818"
   :base08 "ab4642"
   :base09 "dc9656"
   :base0A "f7ca88"
   :base0B "a1b56c"
   :base0C "86c1b9"
   :base0D "7cafc2"
   :base0E "ba8baf"
   :base0F "a16946"
   :scheme "Default Light"})

(def base
  {:scheme-slug "default-light"
   :scheme-name "Default Light"
   :scheme-author "Chris Kempson"})

(def expected
  {:scheme-slug "default-light"
   :scheme-name "Default Light"
   :scheme-author "Chris Kempson"
   :base00-hex "f8f8f8"
   :base01-hex "e8e8e8"
   :base02-hex "d8d8d8"
   :base03-hex "b8b8b8"
   :base04-hex "585858"
   :base05-hex "383838"
   :base06-hex "282828"
   :base07-hex "181818"
   :base08-hex "ab4642"
   :base09-hex "dc9656"
   :base0A-hex "f7ca88"
   :base0B-hex "a1b56c"
   :base0C-hex "86c1b9"
   :base0D-hex "7cafc2"
   :base0E-hex "ba8baf"
   :base0F-hex "a16946"
   :base00-hex-r "f8"
   :base01-hex-r "e8"
   :base02-hex-r "d8"
   :base03-hex-r "b8"
   :base04-hex-r "58"
   :base05-hex-r "38"
   :base06-hex-r "28"
   :base07-hex-r "18"
   :base08-hex-r "ab"
   :base09-hex-r "dc"
   :base0A-hex-r "f7"
   :base0B-hex-r "a1"
   :base0C-hex-r "86"
   :base0D-hex-r "7c"
   :base0E-hex-r "ba"
   :base0F-hex-r "a1"
   :base00-hex-g "f8"
   :base01-hex-g "e8"
   :base02-hex-g "d8"
   :base03-hex-g "b8"
   :base04-hex-g "58"
   :base05-hex-g "38"
   :base06-hex-g "28"
   :base07-hex-g "18"
   :base08-hex-g "46"
   :base09-hex-g "96"
   :base0A-hex-g "ca"
   :base0B-hex-g "b5"
   :base0C-hex-g "c1"
   :base0D-hex-g "af"
   :base0E-hex-g "8b"
   :base0F-hex-g "69"
   :base00-hex-b "f8"
   :base01-hex-b "e8"
   :base02-hex-b "d8"
   :base03-hex-b "b8"
   :base04-hex-b "58"
   :base05-hex-b "38"
   :base06-hex-b "28"
   :base07-hex-b "18"
   :base08-hex-b "42"
   :base09-hex-b "56"
   :base0A-hex-b "88"
   :base0B-hex-b "6c"
   :base0C-hex-b "b9"
   :base0D-hex-b "c2"
   :base0E-hex-b "af"
   :base0F-hex-b "46"
   :base00-rgb-r "248"
   :base01-rgb-r "232"
   :base02-rgb-r "216"
   :base03-rgb-r "184"
   :base04-rgb-r "88"
   :base05-rgb-r "56"
   :base06-rgb-r "40"
   :base07-rgb-r "24"
   :base08-rgb-r "171"
   :base09-rgb-r "220"
   :base0A-rgb-r "247"
   :base0B-rgb-r "161"
   :base0C-rgb-r "134"
   :base0D-rgb-r "124"
   :base0E-rgb-r "186"
   :base0F-rgb-r "161"
   :base00-rgb-g "248"
   :base01-rgb-g "232"
   :base02-rgb-g "216"
   :base03-rgb-g "184"
   :base04-rgb-g "88"
   :base05-rgb-g "56"
   :base06-rgb-g "40"
   :base07-rgb-g "24"
   :base08-rgb-g "70"
   :base09-rgb-g "150"
   :base0A-rgb-g "202"
   :base0B-rgb-g "181"
   :base0C-rgb-g "193"
   :base0D-rgb-g "175"
   :base0E-rgb-g "139"
   :base0F-rgb-g "105"
   :base00-rgb-b "248"
   :base01-rgb-b "232"
   :base02-rgb-b "216"
   :base03-rgb-b "184"
   :base04-rgb-b "88"
   :base05-rgb-b "56"
   :base06-rgb-b "40"
   :base07-rgb-b "24"
   :base08-rgb-b "66"
   :base09-rgb-b "86"
   :base0A-rgb-b "136"
   :base0B-rgb-b "108"
   :base0C-rgb-b "185"
   :base0D-rgb-b "194"
   :base0E-rgb-b "175"
   :base0F-rgb-b "70"})

(def expected-base00
  {:base00-hex "f8f8f8"
   :base00-hex-r "f8"
   :base00-hex-g "f8"
   :base00-hex-b "f8"
   :base00-rgb-r "248"
   :base00-rgb-g "248"
   :base00-rgb-b "248"
   :base00-rgbf-r "0.972549"
   :base00-rgbf-g "0.972549"
   :base00-rgbf-b "0.972549"
   :base00 {:hex "f8f8f8"
            :hex-r "f8"
            :hex-g "f8"
            :hex-b "f8"
            :rgb-r "248"
            :rgb-g "248"
            :rgb-b "248"
            :rgbf-r "0.972549"
            :rgbf-g "0.972549"
            :rgbf-b "0.972549"}})

(defn submap? [a b]
  (clojure.set/subset? (set a) (set b)))

(deftest assoc-bases
  (testing "hex-components"
    (testing "extracts components from a hex string"
      (is (= (sut/hex-components "abcdef") ["ab" "cd" "ef"]))))
  (testing "rgb-components"
    (testing "extracts components from a hex string"
      (is (= (sut/rgb-components "abcdef") [171 205 239]))))
  (testing "color-pairs"
    (testing "returns a map for a single color"
      (is (= (sut/color-map scheme-desc :base00) expected-base00))))
  (testing "assoc-bases"
    (testing "adds colors to the base object"
      (is (submap? expected (sut/assoc-bases base scheme-desc))))))

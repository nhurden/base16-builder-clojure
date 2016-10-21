(defproject base16-builder-clojure "0.1.0-SNAPSHOT"
  :description "A base16 builder in clojure"
  :url "http://github.com/nhurden/base16-builder-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [clj-yaml "0.4.0"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [me.raynes/conch "0.8.0"]
                 [me.raynes/fs "1.4.6"]
                 [slugger "1.0.1"]]
  :main ^:skip-aot base16-builder-clojure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

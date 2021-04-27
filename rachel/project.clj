(defproject phone-number "0.1.0-SNAPSHOT"
  :description "Implementation of Erann Gat's phone number problem"
  :url "https://github.com/rachbowyer/erann-gat-phone-number/reference"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [lein-vanity "0.2.0"]]
  :main ^:skip-aot phone-number.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

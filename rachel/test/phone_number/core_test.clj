(ns phone-number.core-test
  (:require
    [clojure.string :as str]
    [clojure.java.io :as io]
    [clojure.test :refer :all]
    [phone-number.core :refer :all]))

(defn create-test-dictionary []
  (let [word-list (-> "sample-dictionary.txt"
                      io/resource
                      slurp
                      str/split-lines)]
         (create-dictionary (create-letter->digit-map) word-list)))

(deftest word->digits-test
  (are [result arg] (= result (word->digits (create-letter->digit-map) arg))
       "90888" "hello"
       "562"   "mir"
       "482"   "Tor"
       "83"    "o\"d"))

(deftest create-dictionary-test
  (let [dictionary (create-test-dictionary)]
    (is (= ["fort" "Torf"] (get dictionary  "4824")))))

(deftest clean-number-test
  (are [result arg] (= result (clean-number arg))
       "12" "1-2"
       "12" "12"))

(deftest encode-number-wrapped-test
  (are [result arg] (= result
                       (encode-number-wrapped (create-test-dictionary) arg))
    ["5624-82: mir Tor" "5624-82: Mix Tor"] "5624-82"
    nil                                     "112"))
(ns phone-number.core
  (:gen-class)
  (:require
    [clojure.string :as str]))

;E | J N Q | R W X | D S Y | F T | A M | C I V | B K U | L O P | G H Z
;e | j n q | r w x | d s y | f t | a m | c i v | b k u | l o p | g h z
;0 |   1   |   2   |   3   |  4  |  5  |   6   |   7   |   8   |   9

(def letter->digit
  {[\E \e]                "0"
   [\J \N \Q  \j \n \q]   "1"
   [\R \W \X \r \w \x]    "2"
   [\D \S \Y \d \s \y]    "3"
   [\F \T \f \t]          "4"
   [\A \M \a \m]          "5"
   [\C \I \V \c \i \v]    "6"
   [\B \K \U \b \k \u]    "7"
   [\L \O \P \l \o \p]    "8"
   [\G \H \Z \g \h \z]    "9"})

(defn create-letter->digit-map
  "Map of each letter (in a word) to a digit"
  []
  (->> letter->digit
       (mapcat (fn [[k v]] (map (fn [e] [e  v])  k)))
       (into {})))

(defn word->digits
  "Finds the phone number a given word represents
    letter->digits is a map of each letter to a digit"
  [letter->digit-map word]
  (->> word
       (map #(get letter->digit-map % ""))
       str/join))

(defn create-dictionary
  "Creates a lookup hashmap from a number to all the words
   that can encode it.
    letter->digits is a map of each letter to a digit"
  [letter->digit-map word-list]
  (reduce
    (fn [acc e]
      (let [encoded-word (word->digits letter->digit-map e)]
        (if (get acc encoded-word)
          (update acc encoded-word conj e)
          (assoc acc encoded-word [e]))))
    {}
    word-list))

(defn encode-number
  "Given a dictionary and number returns a list of encodings of the number
    encode-number-memo - is a memoized version of the encode number function
    allowed-digit? - true if the digit can be substituted for the encoding
    number - should be stripped of non-digits"
  [encode-number-memo dictionary allowed-digit? number]
  (if (empty? number)
    []

    (let [array-prefixes (map (fn [index]
                                (let [prefix (subs number 0 (inc index))]
                                  (or (get dictionary prefix) [])))
                              (range (count number)))
          use-digit (and allowed-digit? (empty? (remove empty? array-prefixes)))
          array-prefixes (if use-digit [[(subs number 0 1)]] array-prefixes)]


      (mapcat (fn [prefixes index]
                (let [suffix (subs number (inc index))]
                  (cond
                    (empty? prefixes)
                    []

                    (empty? suffix)
                    (map (fn [e] [e]) prefixes)

                    :else
                    (let [suffixes (encode-number-memo encode-number-memo
                                                       dictionary
                                                       (not use-digit)
                                                       suffix)]
                      (for [p prefixes s suffixes] (cons p s))))))
              array-prefixes (range)))))


(defn clean-number
  "Strips out everything in the phone number which is not a digit"
  [number]
  (->> number
       (filter #(<= (int \0) (int %) (int \9)))
       str/join))

(defn encode-number-wrapped
  "Given dictionary and number, returns a list of strings
   containing both the origin number and an encoding of the number"
  [dictionary number]
  ;; Memoize function to allow dynamic programming to kick in
  ;; But do it on each run to ensure that memory usage is kept under control.
  ;; The length of the input is unrestricted
  (let [encode-number-memo (memoize encode-number)
        encoded (encode-number-memo encode-number-memo
                                    dictionary
                                    true
                                    (clean-number number))]
    (when (seq encoded)
      (for [e encoded]
        (str number ": " (str/join " " e))))))

(defn create-dictionary-from-file [filename]
  (let [word-list (-> filename
                      slurp
                      str/split-lines)]
    (create-dictionary (create-letter->digit-map) word-list)))

(defn -main [& args]
  ;; One argument is passed in - the filename of the dictionary file
  (let [dict (create-dictionary-from-file (first args))]
    (loop []
      (when-let [line (read-line)]
        (doseq [n (encode-number-wrapped dict line)]
          (println n))
        (recur)))))

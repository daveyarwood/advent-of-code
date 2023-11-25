(ns advent.2015.12.08.puzzle-1
  (:require [advent.codec    :refer [hex->utf8]]
            [clojure.string  :as    str]
            [clojure.java.io :as    io]))

(defn count-extra-chars
  [s]
  (let [code-chars   (count s)
        string-chars (-> s
                         (str/replace #"\\x([0-9a-f]{2})"
                                      (fn [[_ match]] (hex->utf8 match)))
                         (str/replace #"\\\"" "\"")
                         (str/replace #"\\\\" (str/re-quote-replacement "\\"))
                         count
                         ;; don't count the initial and final "
                         (- 2))]
    (- code-chars string-chars)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-08-01-input"))]
    (->> rdr line-seq (map count-extra-chars) (reduce +) prn)))

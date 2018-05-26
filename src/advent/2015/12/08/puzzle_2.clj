(ns advent.2015.12.08.puzzle-2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn count-extra-chars
  [s]
  (let [string-chars (count s)
        code-chars   (-> s
                         (str/escape {\" "\\\"" \\ "\\\\"})
                         count
                         ;; include the initial and final " in the count
                         (+ 2))]
    (- code-chars string-chars)))

(def s "\"\"")

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-08-01-input"))]
    (->> rdr line-seq (map count-extra-chars) (reduce +) prn)))

(ns advent.2023.12.01.puzzle-1
  (:require [clojure.java.io :as io]))

(defn calibration-value
  [s]
  (->> s
       (filter (set "1234567890"))
       ((juxt first last))
       (apply str)
       parse-long))

(comment
  (map
    calibration-value
    ["1abc2"
     "pqr3stu8vwx"
     "a1b2c3d4e5f"
     "treb7uchet"]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2023-12-01-01-input"))]
    (->> rdr
         line-seq
         (map calibration-value)
         (reduce +)
         println)))

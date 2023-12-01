(ns advent.2023.12.01.puzzle-2
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn str->num
  [s]
  (case s
    "zero"  0
    "one"   1
    "two"   2
    "three" 3
    "four"  4
    "five"  5
    "six"   6
    "seven" 7
    "eight" 8
    "nine"  9
    (parse-long s)))

(defn- hacky-replacements-to-avoid-edge-cases
  "See https://www.reddit.com/r/adventofcode/comments/1883ibu/comment/kbielt0/"
  [s]
  (-> s
      (str/replace "zero" "zero0zero")
      (str/replace "one" "one1one")
      (str/replace "two" "two2two")
      (str/replace "three" "three3three")
      (str/replace "four" "four4four")
      (str/replace "five" "five5five")
      (str/replace "six" "six6six")
      (str/replace "seven" "seven7seven")
      (str/replace "eight" "eight8eight")
      (str/replace "nine" "nine9nine")))

(defn calibration-value
  [s]
  (->> s
       hacky-replacements-to-avoid-edge-cases
       (re-seq #"\d|zero|one|two|three|four|five|six|seven|eight|nine")
       ((juxt first last))
       (map str->num)
       (apply str)
       parse-long))

(comment
  (map
    calibration-value
    ["two1nine"
     "eighttwothree"
     "abcone2threexyz"
     "xtwone3four"
     "4nineeightseven2"
     "zoneight234"
     "7pqrstsixteen"])

  (calibration-value "eighthree"))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2023-12-01-01-input"))]
    (->> rdr
         line-seq
         (map calibration-value)
         (reduce +)
         println)))

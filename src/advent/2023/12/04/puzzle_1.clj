(ns advent.2023.12.04.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.math    :as math]
            [clojure.set     :as set]
            [clojure.string  :as str]))

(defn- parse-numbers
  [string]
  (set (map parse-long (str/split string #"\s+"))))

(defn parse-card
  [string]
  (let [[_ id winning-numbers your-numbers]
        (re-matches
          #"Card\s+(\d+): (.*) \| (.*)"
          string)]
    {:id              (parse-long id)
     :winning-numbers (parse-numbers winning-numbers)
     :your-numbers    (parse-numbers your-numbers)}))

(comment
  ;;=> {:id 1,
  ;;    :winning-numbers #{86 48 41 17 83},
  ;;    :your-numbers #{86 48 31 6 17 9 83 53}}
  (parse-card
    "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"))

(defn score-card
  [{:keys [winning-numbers your-numbers]}]
  (->> (set/intersection winning-numbers your-numbers)
       count
       dec
       (math/pow 2)
       long))

(comment
  ;;=> 8
  (score-card
    {:id              1
     :winning-numbers #{41 48 83 86 17}
     :your-numbers    #{83 86 6 31 17 9 48 53}}))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2023-12-04-01-input"))]
    (->> rdr
         line-seq
         (reduce (fn [sum line]
                   (let [card (parse-card line)]
                     (+ sum (score-card card))))
                 0)
         prn)))

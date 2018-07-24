(ns advent.2016.12.05.puzzle-1
  (:require [clojure.string :as str]
            [digest]))

(defn generate-password
  [door-id]
  (->> (range)
       (map #(digest/md5 (str door-id %)))
       (filter #(str/starts-with? % "00000"))
       (map #(nth % 5))
       (take 8)
       (apply str)))

(def door-id "uqwqemis")

(defn -main
  []
  (-> door-id generate-password println))

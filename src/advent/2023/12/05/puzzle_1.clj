(ns advent.2023.12.05.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn- parse-numbers
  [string]
  (as-> string ?
    (str/trim ?)
    (str/split ? #"\s+")
    (map parse-long ?)))

(defn- parse-map-entry
  [string]
  (zipmap
    [:destination-range-start :source-range-start :range-length]
    (parse-numbers string)))

(defn parse-almanac
  [input]
  (let [[_ seeds & maps]
        (re-matches
          (re-pattern
            (str
              "seeds: (.*)\\n\\n"
              "seed-to-soil map:\\n([0-9\\s]*)"
              "soil-to-fertilizer map:\\n([0-9\\s]*)"
              "fertilizer-to-water map:\\n([0-9\\s]*)"
              "water-to-light map:\\n([0-9\\s]*)"
              "light-to-temperature map:\\n([0-9\\s]*)"
              "temperature-to-humidity map:\\n([0-9\\s]*)"
              "humidity-to-location map:\\n([0-9\\s]*)"))
          input)]
    (merge
      {:seeds (parse-numbers seeds)}
      (zipmap
        [:seed->soil
         :soil->fertilizer
         :fertilizer->water
         :water->light
         :light->temperature
         :temperature->humidity
         :humidity->location]
        (for [almanac-map maps]
          (as-> almanac-map ?
            (str/trim ?)
            (str/split-lines ?)
            (map parse-map-entry ?)))))))

(defn- find-map-entry
  [almanac-map source-number]
  (->> almanac-map
       (filter (fn [{:keys [source-range-start range-length]}]
                 (<= source-range-start
                     source-number
                     (+ source-range-start (dec range-length)))))
       first))

(defn- lookup-fn
  [map-key]
  (fn [almanac source-number]
    (let [almanac-map
          (get almanac map-key)

          {:keys [source-range-start destination-range-start] :as entry}
          (find-map-entry almanac-map source-number)]
      (if entry
        (+ destination-range-start
           (- source-number source-range-start))
        source-number))))

(def seed->soil (lookup-fn :seed->soil))
(def soil->fertilizer (lookup-fn :soil->fertilizer))
(def fertilizer->water (lookup-fn :fertilizer->water))
(def water->light (lookup-fn :water->light))
(def light->temperature (lookup-fn :light->temperature))
(def temperature->humidity (lookup-fn :temperature->humidity))
(def humidity->location (lookup-fn :humidity->location))

(defn seed->location
  [almanac seed-number]
  (->> seed-number
       (seed->soil almanac)
       (soil->fertilizer almanac)
       (fertilizer->water almanac)
       (water->light almanac)
       (light->temperature almanac)
       (temperature->humidity almanac)
       (humidity->location almanac)))

(comment
  (def example-input
    "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4")

  (def almanac
    (parse-almanac example-input))

  (map #(seed->soil almanac %)
       (:seeds almanac))

  (map #(seed->location almanac %)
       (:seeds almanac)))

(defn -main
  []
  (let [almanac (-> (io/resource "2023-12-05-01-input") slurp parse-almanac)]
    (->> almanac
         :seeds
         (map #(seed->location almanac %))
         (apply min)
         prn)))

(ns advent.2023.12.05.puzzle-2
  (:require [advent.2023.12.05.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(comment
  "This second part exceeded my pain threshold, so I punted. But I at least
   got far enough to implement the brute force solution (which takes longer to
   run than I'm willing to wait) and realize that I would need to write some
   fairly sophisticated code dealing with overlapping ranges.")

(defn parse-almanac
  [input]
  (-> input
      p1/parse-almanac
      (update
        :seeds
        (fn [seed-ranges]
          (->> seed-ranges
               (partition-all 2)
               ;; This is the brute force way: generate a huge list of seed
               ;; numbers using the provided ranges.
               (mapcat
                 (fn [[range-start range-length]]
                   (range range-start (+ range-start range-length))))
               ;; This is what I would need to do instead. (This is as far as I
               ;; got before I punted.)
               #_(map (fn [[start length]]
                        {:range-start  start
                         :range-length length})))))))

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

  (map #(p1/seed->soil almanac %)
       (:seeds almanac))

  (map #(p1/seed->location almanac %)
       (:seeds almanac)))

(defn -main
  []
  (let [almanac (-> (io/resource "2023-12-05-01-input") slurp parse-almanac)]
    (->> almanac
         :seeds
         (pmap #(p1/seed->location almanac %))
         (apply min)
         prn)))

(ns advent.2016.12.16.puzzle-1)

(def initial-state "11100010111110100")
(def disk-length 272)

(defn curve
  [input]
  (str input
       \0
       (->> input
            reverse
            (map #(if (#{\0} %) \1 \0))
            (apply str))))

(defn fill-disk-length
  [length input]
  (if (>= (count input) length)
    (->> input (take length) (apply str))
    (recur length (curve input))))

(defn checksum
  [data]
  (let [checksum (->> data
                      (partition 2)
                      (map (fn [[a b]] (if (= a b) \1 \0)))
                      (apply str))]
    (if (even? (count checksum))
      (recur checksum)
      checksum)))

(defn -main
  []
  (->> initial-state
       (fill-disk-length disk-length)
       checksum
       println))


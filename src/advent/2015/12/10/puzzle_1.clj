(ns advent.2015.12.10.puzzle-1)

(def input "1113222113")

(defn look-and-say
  [digits]
  (->> digits
       (partition-by identity)
       (mapcat #(list (count %) (first %)))
       (apply str)))

(defn -main
  []
  (->> input (iterate look-and-say) (drop 40) first count prn))

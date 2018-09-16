(ns advent.2016.12.17.puzzle-2
  (:require [advent.2016.12.17.puzzle-1 :as p1]
            [clojure.set                :as set]))

(defn longest-path
  [start-coord end-coord passcode]
  (loop [realities [[start-coord ""]], successes #{}]
    (let [successes  (->> realities
                          (filter (fn [[coord path-taken]]
                                    (= end-coord coord)))
                          set
                          (set/union successes))
          next-steps (->> realities
                          (remove successes)
                          (mapcat (fn [[coord path-taken]]
                                    (p1/options coord
                                                passcode
                                                path-taken))))]
      (if (empty? next-steps)
        (->> successes
             (map second)
             (apply max-key count))
        (recur next-steps successes)))))

(defn -main
  []
  (-> (longest-path [0 0] [3 3] p1/passcode)
      count
      prn))

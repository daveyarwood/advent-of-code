(ns advent.2016.12.17.puzzle-1
  (:require [digest]))

(def passcode "yjjvjgan")

(defn neighbors
  [[x y]]
  (->> {\R [(inc x) y]
        \L [(dec x) y]
        \D [x (inc y)]
        \U [x (dec y)]}
       (filter #(every? (fn [n] (<= 0 n 3)) (val %)))
       (into {})))

(defn doors-open
  [passcode path-taken]
  (let [[up down left right] (->> (str passcode path-taken)
                                  digest/md5
                                  (take 4))]
    (for [[direction character]
          [[\U up] [\D down] [\L left] [\R right]]
          :when (#{\b \c \d \e \f} character)]
      direction)))

(defn options
  [coord passcode path-taken]
  (let [neighbors (neighbors coord)]
    (for [direction (doors-open passcode path-taken)
          :let [new-coord (get neighbors direction)]
          :when new-coord]
      [new-coord (str path-taken direction)])))

(defn shortest-path
  [start-coord end-coord passcode]
  (loop [realities [[start-coord ""]]]
    (or (some (fn [[coord path-taken]]
                (when (= end-coord coord)
                  path-taken))
              realities)
        (let [next-steps (mapcat (fn [[coord path-taken]]
                                   (options coord passcode path-taken))
                                 realities)]
          (if (empty? next-steps)
            next-steps
            (recur next-steps))))))

(defn -main
  []
  (println (shortest-path [0 0] [3 3] passcode)))

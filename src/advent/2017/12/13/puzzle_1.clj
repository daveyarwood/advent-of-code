(ns advent.2017.12.13.puzzle-1
  (:require [clojure.java.io :as io]))

(defn parse-layer
  [s]
  (->> s
       (re-matches #"(\d+):\s*(\d+)")
       rest
       (map #(Integer/parseInt %))))

(defn initialize-firewall
  [layers]
  (loop [firewall [], i 0, [[depth range :as layer] & more :as layers] layers]
    (cond
      (not layer)
      firewall

      (not= i depth)
      (recur (conj firewall nil) (inc i) layers)

      :else
      (let [layer {:range             range
                   :scanner-position  0
                   :scanner-direction 1}] ; 1 = increase, -1 = decrease
        (recur (conj firewall layer) (inc i) more)))))

(defn tick
  [firewall]
  (-> (for [depth firewall]
        (when-let [{:keys [range scanner-position scanner-direction] :as layer}
                   depth]
          (let [direction
                (cond
                  (and (zero? scanner-position)
                       (neg? scanner-direction))
                  (- scanner-direction)

                  (and (= (dec range) scanner-position)
                       (pos? scanner-direction))
                  (- scanner-direction)

                  :else
                  scanner-direction)

                position
                (+ scanner-position direction)]
            (assoc layer
                   :scanner-position position
                   :scanner-direction direction))))
      vec))

(defn trip-severity
  [ticks]
  (reduce (fn [[severity depth] layers]
            (if (= (count layers) depth)
              (reduced severity)
              (let [{:keys [scanner-position range]} (nth layers depth)]
                [(if (= 0 scanner-position)
                   (+ severity (* depth range))
                   severity)
                 (inc depth)])))
          [0 0]
          ticks))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-13-01-input"))]
    (->> rdr
         line-seq
         (map parse-layer)
         initialize-firewall
         (iterate tick)
         trip-severity
         prn)))

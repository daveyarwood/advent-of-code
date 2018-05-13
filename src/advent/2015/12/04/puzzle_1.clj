(ns advent.2015.12.04.puzzle-1
  (:require [clojure.string :as str]
            [digest]))

(def secret-key
  "ckczppom")

(def difficulty
  "The number of leading zeroes required for a coin to be considered mined."
  5)

(defn mined?
  [difficulty secret-key nonce]
  (let [hash   (-> secret-key (str nonce) digest/md5)
        zeroes (->> \0 (repeat difficulty) (apply str))]
    (str/starts-with? hash zeroes)))

(defn mine
  [difficulty secret-key]
  (->> (range)
       (drop-while #(not (mined? difficulty secret-key %)))
       first))

(defn -main
  []
  (prn (mine difficulty secret-key)))

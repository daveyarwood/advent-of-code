(ns advent.2016.12.05.puzzle-2
  (:require [clojure.string :as str]
            [digest]))

(defn generate-password
  [door-id]
  (println "________")
  (loop [password '[_ _ _ _ _ _ _ _]
         nonce    0]
    (when (some '#{_} password)
      (let [md5-hash (digest/md5 (str door-id nonce))]
        (if (re-matches #"^0{5}[0-7].*" md5-hash)
          (let [[_ _ _ _ _ i c] md5-hash
                i               (Character/digit i 10)
                new-password    (when (= '_ (nth password i))
                                  (assoc password i c))]
            (if new-password
              (do
                (println (apply str new-password))
                (recur new-password (inc nonce)))
              (recur password (inc nonce))))
          (recur password (inc nonce)))))))

(def door-id "uqwqemis")

(defn -main
  []
  (-> door-id generate-password))


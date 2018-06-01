(ns advent.2015.12.11.puzzle-1
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def ALPHABET "abcdefghijklmnopqrstuvwxyz")

(defn inc-letter
  [letter]
  (if (= \z letter) \a (-> letter int inc char)))

(defn inc-password*
  [reversed-password]
  (let [[letter & more] reversed-password
        letter (when letter (inc-letter letter))]
    (cond
      (and (= \a letter) (seq more))
      (cons letter (inc-password* more))

      (= \a letter)
      ;; if a is like 0, then b is like 1
      ;; in other words, if we're incrementing 9 and we get 0, then we want 10
      ;; (and it's temporarily reversed, so this is 01)
      '(\a \b)

      :else
      (cons letter more))))

(defn inc-password
  [password]
  (->> password reverse inc-password* reverse (apply str)))

(defn valid-password?
  [password]
  (let [straights (->> ALPHABET (partition 3 1) (map #(apply str %)))
        pairs     (->> ALPHABET (map (partial repeat 2)) (map #(apply str %)))]
    (and (some (partial str/includes? password) straights)
         (= (count (set password))
            (count (set/difference (set password) #{\i \o \l})))
         (->> pairs
              (filter (partial str/includes? password))
              count
              (<= 2)))))

(defn next-password
  [password]
  (->> password
       (iterate inc-password)
       rest
       (drop-while (complement valid-password?))
       first))

(def input "vzbxkghb")

(defn -main
  []
  (-> input next-password prn))

(ns advent.2016.12.10.puzzle-1
  (:require [clojure.core.match :refer (match)]
            [clojure.java.io    :as    io]))

(defn parse-instruction
  [s]
  (or (some->> s
               (re-matches #"value (\d+) goes to bot (\d+)")
               rest
               (map #(Integer/parseInt %))
               ((fn [[value bot]]
                  [:init-bot bot value])))
      (some->> s
               (re-matches
                 (re-pattern
                   (str "bot (\\d+) gives low to (bot|output) (\\d+) "
                        "and high to (bot|output) (\\d+)")))
               rest
               ((fn [[bot
                      low-target-type low-target
                      high-target-type high-target]]
                  [:transfer (Integer/parseInt bot)
                   [(keyword low-target-type) (Integer/parseInt low-target)]
                   [(keyword high-target-type) (Integer/parseInt high-target)]])))
      (throw (ex-info "Unrecognized input." {:input s}))))

(def expected-values [17 61])

;; A bot will deliver its number here when it is responsible for comparing the
;; two expected values.
(def solution (promise))

(def state (ref {}))

(defn remove-first
  [coll value]
  (->> coll
       (reduce (fn [[coll removed?] x]
                 (if (and (= value x) (not removed?))
                   [coll true]
                   [(conj coll x) removed?]))
               [(empty coll) false])
       first))

(defn transfer
  [source target value]
  (dosync
    (when source
      (alter state update source remove-first value))
    (alter state update target (fnil conj []) value)))

(defn await-values
  [source quantity]
  (loop []
    (let [values (-> @state (get source))]
      (if (= quantity (count values))
        values
        (do (Thread/sleep 100) (recur))))))

(defn spawn-bots!
  [instructions]
  (doseq [instruction instructions]
    (future
      (match instruction
        [:init-bot bot value]
        (transfer nil [:bot bot] value)

        [:transfer
         bot
         [low-target-type low-target]
         [high-target-type high-target]]
        (let [[low high :as values] (sort (await-values [:bot bot] 2))]
          (when (= expected-values values)
            (deliver solution [:bot bot]))
          (dosync
            (transfer [:bot bot] [low-target-type low-target] low)
            (transfer [:bot bot] [high-target-type high-target] high)))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-10-01-input"))]
    (println "Spawning bots...")
    (->> rdr line-seq (map parse-instruction) spawn-bots!)
    (println "Waiting for the solution...")
    (prn @solution)))

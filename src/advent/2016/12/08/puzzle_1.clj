(ns advent.2016.12.08.puzzle-1
  (:require [clojure.core.match :refer (match)]
            [clojure.java.io    :as    io]
            [clojure.string     :as    str]))

(defn parse-instruction
  "Returns "
  [s]
  (as-> s ?
    (str/split s #" ")
    (let [[command & args] ?]
      (case command
        "rect"   (let [[width height] (as-> args ?
                                        (first ?)
                                        (str/split ? #"x")
                                        (map #(Integer/parseInt %) ?))]
                   [:rect width height])
        "rotate" (let [[target-type selector _ amount] args
                       i (-> selector (str/split #"=") last Integer/parseInt)]
                   [:rotate target-type i (Integer/parseInt amount)])))))

(defn rotate-row
  "Rotates row `i` of screen to the right by `amount`.

   `amount` can be negative, which results in rotating the row to the left by
   `amount` * -1."
  [screen i amount]
  (let [row         (nth screen i)
        amount      (if (neg? amount)
                      (* amount -1)
                      (- (count row) amount))
        rotated-row (->> row cycle (drop amount) (take (count row)))]
    (concat (take i screen)
            [rotated-row]
            (drop (inc i) screen))))

(defn rotate
  "Rotates the entire screen 90 degrees, so that rows become columns and vice
   versa.

   Does the 90 degree rotation `n` times."
  [screen n]
  (letfn [(rotate-once [screen] (apply map #(reverse %&) screen))]
    (as-> screen ?
      (iterate rotate-once ?)
      (nth ? n))))

(defn follow-instruction
  [screen instruction]
  (match instruction
    [:rect width height]
    (for [[y row] (map-indexed vector screen)]
      (for [[x on?] (map-indexed vector row)]
        (if (and (< y height) (< x width))
          true
          on?)))

    [:rotate target-type i amount]
    (case target-type
      "row"    (-> screen (rotate-row i amount))
      "column" (-> screen (rotate 1) (rotate-row i (- amount)) (rotate 3)))))

(defn blank-screen
  [width height]
  (->> false (repeat width) (repeat height)))

(defn count-lit-pixels
  [screen]
  (->> screen
       (map #(->> % (filter identity) count))
       (apply +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-08-01-input"))]
    (->> rdr
         line-seq
         (map parse-instruction)
         (reduce follow-instruction (blank-screen 50 6))
         count-lit-pixels
         prn)))

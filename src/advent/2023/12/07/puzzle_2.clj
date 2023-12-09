(ns advent.2023.12.07.puzzle-2
  (:require [advent.2023.12.07.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]))

(def ^:private card-labels ; ordered weakest -> strongest
  ;; Now J ("joker") is the weakest, for the purpose of comparing cards
  (seq "J23456789TQKA"))


(defn compare-cards
  "Returns 1 if `card-1` is stronger, -1 if `card-2` is stronger, or 0 if they
   are equally strong (i.e. same label)."
  [card-1 card-2]
  (compare (.indexOf card-labels card-1)
           (.indexOf card-labels card-2)))

(comment
  ;;=> 1
  (compare-cards \T \J))

(defn hand-type
  [hand]
  (let [{jokers \J, non-jokers nil}
        (group-by #{\J} hand)

        wildcard-values
        (rest card-labels)

        possible-hands
        (for [wildcards (combo/combinations
                          (->> wildcard-values
                               cycle
                               (take (* (count wildcard-values)
                                        (count jokers))))
                          (count jokers))]
          (concat non-jokers wildcards))]
    (->> possible-hands
         (map p1/hand-type)
         (sort p1/compare-hand-types)
         last)))

(comment
  ;; Both J's can now serve as Q's here
  ;;=> :four-of-a-kind
  (hand-type "QJJQ2"))

(defn compare-hands
  "Returns 1 if `hand-1` is stronger, -1 if `hand-2` is stronger, or 0 if they
   are equally strong."
  [hand-1 hand-2]
  (p1/compare-hands
    hand-1
    hand-2
    {:hand-type-fn     hand-type
     :compare-cards-fn compare-cards}))

(comment
  ;;=> 0
  (compare-hands "AAAAA" "AAAAA")
  ;;=> 1
  (compare-hands "33332" "2AAAA")
  ;;=> -1
  (compare-hands "77888" "77788")

  ;; Ordered weakest -> strongest
  ;; (The ranking is different now because the J's serve as different cards.)
  ;;=> ("32T3K" "KK677" "T55J5" "QQQJA" "KTJJT")
  (sort
    compare-hands
    ["32T3K" "T55J5" "KK677" "KTJJT" "QQQJA"]))

(defn rank-hands
  [hands]
  (p1/rank-hands hands {:compare-hands-fn compare-hands}))

(def ^:private example-input
  "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483")

(comment
  ;;=> ({:hand "32T3K", :bid 765, :rank 1}
  ;;    {:hand "KK677", :bid 28, :rank 2}
  ;;    {:hand "T55J5", :bid 684, :rank 3}
  ;;    {:hand "QQQJA", :bid 483, :rank 4}
  ;;    {:hand "KTJJT", :bid 220, :rank 5})
  (-> example-input p1/parse-hands rank-hands)

  ;;=> 5905
  (->> example-input
       p1/parse-hands
       rank-hands
       (reduce
         (fn [sum {:keys [bid rank]}]
           (+ sum (* bid rank)))
         0)))

(defn -main
  []
  (->> (io/resource "2023-12-07-01-input")
       slurp
       p1/parse-hands
       rank-hands
       (reduce
         (fn [sum {:keys [bid rank]}]
           (+ sum (* bid rank)))
         0)
       prn))

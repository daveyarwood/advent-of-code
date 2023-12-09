(ns advent.2023.12.07.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(def ^:private card-labels ; ordered weakest -> strongest
  (seq "23456789TJQKA"))

(defn compare-cards
  "Returns 1 if `card-1` is stronger, -1 if `card-2` is stronger, or 0 if they
   are equally strong (i.e. same label)."
  [card-1 card-2]
  (compare (.indexOf card-labels card-1)
           (.indexOf card-labels card-2)))

(comment
  ;;=> 1
  (compare-cards \A \T)
  ;;=> 0
  (compare-cards \T \T)
  ;;=> -1
  (compare-cards \2 \Q))

(def ^:private hand-types ; ordered weakest -> strongest
  [{:name :high-card, :groups [1 1 1 1 1]}
   {:name :one-pair, :groups [2 1 1 1]}
   {:name :two-pair, :groups [2 2 1]}
   {:name :three-of-a-kind, :groups [3 1 1]}
   {:name :full-house, :groups [3 2]}
   {:name :four-of-a-kind, :groups [4 1]}
   {:name :five-of-a-kind, :groups [5]}])

(def ^:private hand-type-names
  (map :name hand-types))

(defn compare-hand-types
  "Returns 1 if `hand-type-1` is stronger, -1 if `hand-type-2` is stronger, or
   0 if they are equally strong."
  [hand-type-1 hand-type-2]
  (compare (.indexOf hand-type-names hand-type-1)
           (.indexOf hand-type-names hand-type-2)))

(comment
  ;;=> 1
  (compare-hand-types :full-house :three-of-a-kind)
  ;;=> 0
  (compare-hand-types :full-house :full-house)
  ;;=> -1
  (compare-hand-types :full-house :five-of-a-kind))

(def ^:private groups->hand-type
  (->> hand-types
       (map (fn [{:keys [name groups]}]
              [groups name]))
       (into {})))

(defn hand-type
  [hand]
  (->> hand
       (group-by identity)
       (map (fn [[_label cards]] (count cards)))
       (sort >)
       groups->hand-type))

(comment
  (for [hand ["AAAAA" "AA8AA" "23332" "TTT98" "23432" "A23A4" "23456"]]
    [hand (hand-type hand)]))

(defn compare-hands
  "Returns 1 if `hand-1` is stronger, -1 if `hand-2` is stronger, or 0 if they
   are equally strong (i.e. the exact same hand)."
  [hand-1 hand-2 & [{:keys [hand-type-fn compare-cards-fn]
                     :or {hand-type-fn     hand-type
                          compare-cards-fn compare-cards}}]]
  (let [[type-1 type-2]      (map hand-type-fn [hand-1 hand-2])
        hand-type-comparison (compare-hand-types type-1 type-2)]
    (if-not (zero? hand-type-comparison)
      hand-type-comparison
      (loop [[card-1 & more-1] hand-1
             [card-2 & more-2] hand-2]
        (let [card-comparison (compare-cards-fn card-1 card-2)]
          (if (and (zero? card-comparison)
                   (seq more-1))
            (recur more-1 more-2)
            card-comparison))))))

(comment
  ;;=> 0
  (compare-hands "AAAAA" "AAAAA")
  ;;=> 1
  (compare-hands "33332" "2AAAA")
  ;;=> 1
  (compare-hands "77888" "77788")

  ;; Ordered weakest -> strongest
  ;;=> ("32T3K" "KTJJT" "KK677" "T55J5" "QQQJA")
  (sort
    compare-hands
    ["32T3K" "T55J5" "KK677" "KTJJT" "QQQJA"]))

(defn parse-hands
  [input]
  (->> input
       str/split-lines
       (map #(let [[hand bid] (str/split % #"\s+")]
               {:hand hand, :bid (parse-long bid)}))))

(defn rank-hands
  [hands & [{:keys [compare-hands-fn]
             :or {compare-hands-fn compare-hands}}]]
  (->> hands
       (sort-by :hand compare-hands-fn)
       (map-indexed
         (fn [i hand]
           (assoc hand :rank (inc i))))))

(def ^:private example-input
  "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483")

(comment
  ;;=> ({:hand "32T3K", :bid 765}
  ;;    {:hand "T55J5", :bid 684}
  ;;    {:hand "KK677", :bid 28}
  ;;    {:hand "KTJJT", :bid 220}
  ;;    {:hand "QQQJA", :bid 483})
  (parse-hands example-input)


  ;;=> ({:hand "32T3K", :bid 765, :rank 1}
  ;;    {:hand "KTJJT", :bid 220, :rank 2}
  ;;    {:hand "KK677", :bid 28, :rank 3}
  ;;    {:hand "T55J5", :bid 684, :rank 4}
  ;;    {:hand "QQQJA", :bid 483, :rank 5})
  (-> example-input parse-hands rank-hands)

  ;;=> 6440
  (->> example-input
       parse-hands
       rank-hands
       (reduce
         (fn [sum {:keys [bid rank]}]
           (+ sum (* bid rank)))
         0)))

(defn -main
  []
  (->> (io/resource "2023-12-07-01-input")
       slurp
       parse-hands
       rank-hands
       (reduce
         (fn [sum {:keys [bid rank]}]
           (+ sum (* bid rank)))
         0)
       prn))

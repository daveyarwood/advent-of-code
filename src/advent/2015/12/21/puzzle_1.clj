(ns advent.2015.12.21.puzzle-1
  (:require [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]
            [instaparse.core            :as insta]))

(def items-parser
  (insta/parser
    "items        = section (<newline> <newline> section)* <newline>*
     section      = header (<newline> item)*
     header       = section-name <':'> (<col-sep> col-name)*
     section-name = cell-value
     col-name     = cell-value
     item         = item-name (<col-sep> cell-value)*
     item-name    = cell-value
     cell-value   = cell-chars (space cell-chars)*
     <cell-chars> = #'[A-Za-z0-9+]+'
     col-sep      = <space> <space>+
     <space>      = ' '
     newline      = #'\\n'"))

(defn parse-items
  [input]
  (->> input
       items-parser
       (insta/transform
         {:cell-value   #(apply str %&)
          :item-name    identity
          :section-name identity
          :col-name     identity
          :header       list
          :item         (fn [item-name & values]
                          [item-name
                           (map #(Integer/parseInt %) values)])
          :section      (fn [[section-name & col-names] & items]
                          [section-name
                           (map (fn [[item-name values]]
                                  (merge {"Name" item-name}
                                         (zipmap col-names values)))
                                items)])
          :items        #(into {} %&)})))

(def stats-parser
  (insta/parser
    "stats   = stat (<newline> stat)* <newline>*
     stat    = #'[^:]+' <': '> value
     value   = #'\\d+'
     newline = #'\\n'"))

(defn parse-stats
  [input]
  (->> input
       stats-parser
       (insta/transform
         {:value #(Integer/parseInt %)
          :stat  list
          :stats #(reduce (fn [m [k v]] (assoc m k v))
                          {}
                          %&)})))

(defn attack
  [{attacker-damage "Damage" attacker-armor "Armor" :as attacker}
   {defender-damage "Damage" defender-armor "Armor" :as defender}]
  (let [damage-dealt (-> attacker-damage (- defender-armor) (max 1))]
    (update defender "Hit Points" - damage-dealt)))

(defn battle
  ([player boss] (battle player boss true))
  ([{player-hp "Hit Points" :as player}
    {boss-hp "Hit Points" :as boss}
    player-turn?]
   (cond
     (<= player-hp 0) :boss
     (<= boss-hp 0)   :player
     :else            (let [[player boss] (if player-turn?
                                            [player (attack player boss)]
                                            [(attack boss player) boss])]
                        (recur player boss (not player-turn?))))))

(defn possible-items
  [{:strs [Weapons Armor Rings] :as items}]
  (let [possible-weapons (map list Weapons) ; must have 1 weapen
        possible-armor   (cons () (map list Armor)) ; can have 0-1 armor
        possible-rings   (concat [()] ; can have 0-2 rings
                                 (map list Rings)
                                 (combo/combinations Rings 2))]
    (->> [possible-weapons possible-armor possible-rings]
         (apply combo/cartesian-product)
         (map flatten))))

(defn equip
  [player {:strs [Damage Armor] :as item}]
  (-> player
      (update "Damage" + Damage)
      (update "Armor" + Armor)))

(defn player-will-win?
  [player boss items]
  (let [armed-player (reduce equip player items)]
    (= :player (battle armed-player boss))))

(defn total-cost
  [items]
  (->> items
       (map #(get % "Cost"))
       (apply +)))

(def player {"Hit Points" 100 "Damage" 0 "Armor" 0})

(defn -main
  []
  (with-open [items-rdr (io/reader (io/resource "2015-12-21-01-items"))
              stats-rdr (io/reader (io/resource "2015-12-21-01-stats"))]
    (let [items (-> items-rdr slurp parse-items)
          boss  (-> stats-rdr slurp parse-stats)]
      (->> items
           possible-items
           (filter (partial player-will-win? player boss))
           (apply min-key total-cost)
           total-cost
           prn))))


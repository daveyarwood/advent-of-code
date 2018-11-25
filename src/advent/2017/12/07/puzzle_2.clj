(ns advent.2017.12.07.puzzle-2
  (:require [advent.2017.12.07.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(comment
  "This didn't yield the right answer; I don't think I fully understand what the
   problem is asking for.")

(defn nodes-map
  "Given a list of nodes, returns a map of node names to nodes."
  [nodes]
  (reduce (fn [m node] (assoc m (:name node) node)) {} nodes))

(defn siblings-map
  "Given the output of p1/parents-map, returns a map where each key is the name
   of a node and the value is the set of the node's siblings."
  [parents-map]
  (into {}
    (for [[node parent] parents-map
          :let [siblings (set (for [[k v] parents-map
                                    :when (and (not= node k) (= parent v))]
                                k))]]
      [node siblings])))

(defn effective-weight
  [nodes-map {:keys [weight children]}]
  (apply + weight (map (comp (partial effective-weight nodes-map)
                             #(get nodes-map %))
                       children)))

(defn sibling-weights
  [siblings-map weights {:keys [name]}]
  (let [siblings (get siblings-map name)]
    (map #(get weights %) siblings)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-07-01-input"))]
    (let [nodes           (->> rdr line-seq (map p1/parse-node))
          nodes-map       (nodes-map nodes)
          siblings-map    (->> nodes p1/parents-map siblings-map)
          weights         (into {}
                            (map (fn [[k v]]
                                   [k (effective-weight nodes-map v)])
                                 nodes-map))
          sibling-weights (into {}
                            (map (fn [[k v]]
                                   [k (sibling-weights
                                        siblings-map
                                        weights
                                        v)])
                                 nodes-map))]
      (->> (for [node (keys nodes-map)
                 :let [[weight sibling-weights]
                       [(get weights node) (get sibling-weights node)]]
                 :when (and (seq sibling-weights)
                            (not ((set sibling-weights) weight)))]
             {:name            node
              :weight          weight
              :sibling-weights sibling-weights})
           (apply min-key :weight)
           :sibling-weights
           first
           prn))))

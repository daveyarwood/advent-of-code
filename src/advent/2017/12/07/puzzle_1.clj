(ns advent.2017.12.07.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-node
  [s]
  (let [[_ name weight children]
        (re-matches #"(\w+)\s+\((\d+)\)(?:\s+->\s+(.*))?" s)

        weight
        (Integer/parseInt weight)

        children
        (if children
          (set (str/split children #",\s*"))
          #{})]
    {:name name, :weight weight, :children children}))

(defn parents-map
  "Given a list of nodes, returns a map where each key is the name of a node and
   the value is the node's parent."
  [nodes]
  (into {}
    (for [{:keys [name]} nodes
          :let [parent (->> nodes
                            (filter #(contains? (:children %) name))
                            first)]]
      [name (if parent (:name parent) :bottom)])))

(defn bottom-node
  "Given the output of `parents-map`, returns the name of the bottom node."
  [parents-map]
  (let [bottom-nodes (filter #(= :bottom (val %)) parents-map)]
    (when-not (= 1 (count bottom-nodes))
      (throw (ex-info "More/less than 1 bottom node found!"
                      {:bottom-nodes bottom-nodes
                       :parents-map  parents-map})))
    (key (first bottom-nodes))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-07-01-input"))]
    (->> rdr line-seq (map parse-node) parents-map bottom-node println)))

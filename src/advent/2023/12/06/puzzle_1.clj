(ns advent.2023.12.06.puzzle-1)

(def ^:const example-races
  [{:time 7, :distance 9}
   {:time 15, :distance 40}
   {:time 30, :distance 200}])

(def ^:const races
  [{:time 59, :distance 597}
   {:time 79, :distance 1234}
   {:time 65, :distance 1032}
   {:time 75, :distance 1328}])

(defn options
  [{:keys [time]}]
  (for [button-hold-time (range (inc time))
        :let [remaining-time    (- time button-hold-time)
              distance-traveled (* remaining-time button-hold-time)]]
    {:button-hold-time  button-hold-time
     :distance-traveled distance-traveled}))

(defn viable-options
  [{:keys [distance] :as race}]
  (->> (options race)
       (filter (fn [{:keys [distance-traveled]}]
                 (> distance-traveled distance)))))

(comment
  (map options example-races)
  (map viable-options example-races)
  (map (comp count viable-options) example-races)
  (apply * (map (comp count viable-options) example-races)))

(defn -main
  []
  (->> races
       (map (comp count viable-options))
       (apply *)
       prn))

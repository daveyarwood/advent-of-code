(ns advent.2015.12.22.puzzle-1)

(def
  ^{:doc "Each effect can have a :pre fn, which is applied to the target at the
          start of the turn, and a :post fn, which is applied to the target at
          the end of the turn."}
  effects
  {"Shield"   {:pre  #(update % :armor + 7)
               :post #(update % :armor - 7)}
   "Poison"   {:pre #(update % :hp - 3)}
   "Recharge" {:pre #(update % :mana + 101)}})

(defn apply-effect
  [{:keys [effects] :as target} effect turns]
  (let [existing (get effects effect)]
    (if (and existing (not= 0 existing))
      (throw (ex-info "Effect already active."
                      {:category :invalid-spell-choice
                       :spell    effect}))
      (update target :effects assoc effect turns))))

(def spells
  {"Magic Missile" {:cost   53
                    :action (fn [player boss mana-spent]
                              [player
                               (update boss :hp - 4)
                               mana-spent])}
   "Drain"         {:cost   73
                    :action (fn [player boss mana-spent]
                              [(update player :hp + 2)
                               (update boss :hp - 2)
                               mana-spent])}
   "Shield"        {:cost   113
                    :action (fn [player boss mana-spent]
                              [(apply-effect player "Shield" 6)
                               boss
                               mana-spent])}
   "Poison"        {:cost   173
                    :action (fn [player boss mana-spent]
                              [player
                               (apply-effect boss "Poison" 6)
                               mana-spent])}
   "Recharge"      {:cost   229
                    :action (fn [player boss mana-spent]
                              [(apply-effect player "Recharge" 5)
                               boss
                               mana-spent])}})

(defn battle-over
  [state]
  (if (#{:player :boss} (first state))
    state
    (let [[player boss mana-spent] state]
      (cond
        (<= (:hp player) 0) [:boss mana-spent]
        (<= (:hp boss) 0)   [:player mana-spent]))))

(defn apply-effects-pre
  [state]
  (or (battle-over state)
      (let [[player boss mana-spent] state]
        (conj (mapv (fn [{active-effects :effects :as target}]
                      (reduce (fn [target [effect timer]]
                                (let [{:keys [pre] :or {pre identity}}
                                      (get effects effect)]
                                  (-> target
                                      pre
                                      (update-in [:effects effect] dec))))
                              target
                              active-effects))
                    [player boss])
              mana-spent))))

(defn apply-effects-post
  [state & [spell-just-cast]]
  (or (battle-over state)
      (let [[player boss mana-spent] state]
        (conj (mapv (fn [{active-effects :effects :as target}]
                      (-> (reduce (fn [target [effect timer]]
                                    (let [{:keys [post] :or {post identity}}
                                          (get effects effect)]
                                      (post target)))
                                  target
                                  (filter (fn [[effect _]]
                                            (not= spell-just-cast effect))
                                          active-effects))
                          (update :effects #(->> %
                                                 (remove (fn [[effect timer]]
                                                           (zero? timer)))
                                                 (into {})))))
                    [player boss])
              mana-spent))))

(defn cast-spell
  [state spell]
  (or (battle-over state)
      (let [[{:keys [mana] :as player} boss mana-spent] state
            {:keys [name cost action]} (get spells spell)]
        (when (< mana cost)
          (throw (ex-info "Not enough mana to cast spell."
                          {:category :invalid-spell-choice
                           :spell    spell
                           :cost     cost
                           :mana     mana})))
        (->> [(update player :mana - cost) boss (+ mana-spent cost)]
             (apply action)))))

(defn boss-attack
  [state]
  (or (battle-over state)
      (let [[{:keys [armor] :as player}
             {:keys [damage] :as boss}
             mana-spent]
            state]
        [(update player :hp - (-> damage (- armor) (max 1)))
         boss
         mana-spent])))

(defn player-turn
  [state selected-spell]
  (-> state
      apply-effects-pre
      (cast-spell selected-spell)
      ;; This would be a bug if the same spell could be cast on the player and
      ;; the boss, e.g. if Shield were cast on both the player and the boss,
      ;; passing it in here to `apply-effects-post` would mean that the boss's
      ;; shield effect wouldn't go away at the end of this turn, and the next
      ;; turn, the boss would have a double-shield.
      ;;
      ;; This is hacky, but I'm leaving it in because it just so happens that
      ;; any spell is only cast on the player or the boss, so if a spell is cast
      ;; on the player, we can rely on the boss not potentially already having
      ;; that same spell effect.
      (apply-effects-post selected-spell)))

(defn boss-turn
  [state]
  (-> state
      apply-effects-pre
      boss-attack
      apply-effects-post))

(def player
  {:hp      50
   :mana    500
   :effects {}
   ;; wizards can't wear armor, but this value can be augmented via the Shield
   ;; spell
   :armor   0})

(def boss
  {:hp      58
   :damage  9
   :effects {}
   ;; the problem doesn't come out and say that the boss has 0 armor, but the
   ;; way it's described, the boss's armor is implicitly 0 because you're a
   ;; wizard and you cast spells that aren't hampered by armor
   :armor   0})

(defn battle-outcome
  [state [spell & more]]
  (if (and spell (not= [:insufficient-mana] state))
    (recur (try
             (-> state (player-turn spell) boss-turn)
             (catch clojure.lang.ExceptionInfo e
               (if (-> e ex-data :category (= :invalid-spell-choice))
                 [:invalid-spell-choice]
                 (throw e))))
           more)
    state))

(defn analyze-outcome
  [state]
  (if (#{:player :boss :invalid-spell-choice} (first state))
    (first state)
    :incomplete))

(defn cheapest-victory-in-fewest-moves
  [initial-state]
  (loop [incomplete-states [[[] initial-state]]]
    (let [next-states (mapcat (fn [[steps state]]
                                (for [next-step (keys spells)]
                                  [(conj steps next-step)
                                   (battle-outcome state [next-step])]))
                              incomplete-states)
          results     (->> next-states
                           (group-by (fn [[steps state]]
                                       (analyze-outcome state))))]
      (if-let [success-results (:player results)]
        (->> success-results
             (apply min-key (fn [[steps [_ mana-spent]]] mana-spent)))
        (recur (:incomplete results))))))

(defn -main
  []
  (-> [player boss 0] cheapest-victory-in-fewest-moves prn))


(ns game-of-life.core
  (:gen-class))

(def game-board-size-width 10)
(def game-board-size-height 10)

(defn create-new-board
  "Creates a new gameboard with the given dimensions"
  [width height]
  (into {}
        (for [x (range width)
              y (range height)]
          (let [alive? (rand-nth [true false])]
            [[x y] alive?]))))

(defn new-game
  "Returns an initial game state."
  [width height]
  {:width width
   :height height
   :board (create-new-board width height)})

(defn display-board
  "Renders the game to the console."
  [{:keys [height width board]}]
  (doseq [y (range height)]
    (doseq [x (range width)]
      (let [pos [x y]
            alive? (board pos)]
        (print (if alive? \X \space))))
    (println)))

(defn apply-rules-to-position
  [[pos alive?]]
  (cond
    (and alive?
         (> (num-live-neighbors pos) 2))
       [pos false]
    (and alive?
         (>= 3 (num-live-neighbors pos) 2))
       [pos true]
    (and alive?
         (> (num-live-neighbors pos) 3))
       [pos false]
    (and (not alive?)
         (= (num-live-neighbors pos) 3))
       [pos true]))

(defn apply-rules
  [{:keys [board] :as state}]
  (let [new-board (into {} (map apply-rules-to-position board))]
    (assoc state :board new-board)))

(let [game (new-game 10 10)]
  (= game (apply-rules game)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

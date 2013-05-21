(ns clojure-intro-workshop.core
  (:use quil.core)) ;; quil is our drawing library

(defn create-world
  "Return a vector of vectors, which represents a 2D field with the
  given width and height, randomly populated with 0 and 1."
  [width height]
  (for [_ (range height)]
    (for [_ (range width)]
      (rand-int 2))))

(defn world-width [world]
  (count (nth world 0)))

(defn world-height [world]
  (count world))

(defn element-at [world [x y]]
  (if (or (< x 0)
          (< y 0)
          (>= x (world-width world))
          (>= y (world-height world)))
    0 ;; respect array bounds
    (nth (nth world y) x)))

(defn pretty-print-world
  "Handy debugging util."
  [world]
  (doseq [row world]
    (println row)))

;; setting up state
;; --------------------------
(def current-world (agent (create-world 50 40)))

(defn update-world [world]
  ;; This is your chance to change the world! ;)
  world)  ;; return new world

(def tick-delay 2000)

(defn tick [world]
    ;; *agent* is dynamically bound to the agent this function was sent to
    (send *agent* update-world)  ;; queue an update
    (send *agent* tick)  ;; queue the next tick
    (Thread/sleep tick-delay)  ;; delay the queue for a while
    world)  ;; always return a world which becomes the agent's new state

(defn start-main-loop []
  (send current-world tick))

;; rendering functions
;; --------------------------

;; called only once
(defn setup []
  (smooth)
  (frame-rate 20)
  (background 0)) ;; black background

;; called for each frame
(defn draw []
  (stroke 0)
  (stroke-weight 0)
  (let [tile-width (/ (width) (world-width @current-world))
        tile-height (/ (height) (world-height @current-world))]
    (dorun  ;; force immediate evaluation of lazy sequence
      (for [x (range (world-width @current-world))
            y (range (world-height @current-world))]
        (do  ;; we do our drawing side-effects here
          ;; set fill based on cell alive state
          (fill (* 255 (element-at @current-world [x y])))
          ;; draw a rect for each cell
          (rect (* x tile-width) (* y tile-height)
                tile-width tile-height))))))


;; main function
;; --------------------------
(defn -main []
  ;; this is quil starting a drawing cycle
  (defsketch example
    :title "Game of Life"
    :setup setup
    :draw draw
    :size [(* (world-width @current-world) 15),
           (* (world-height @current-world) 15)])
  ;; start the never-ending main loop
  (start-main-loop))

(ns clojure-intro-workshop.core
  (:use quil.core ; GUI rendering library
        [clojure.stacktrace :only [print-stack-trace]]))


(defn create-world
  "Return a vector of vectors, which represents a 2D field with the
  given width and height, randomly populated with 0 and 1."
  [width height]
  (for [_ (range height)]
    (for [_ (range width)]
      (rand-int 2))))


(defn create-blinker
  "Creates a tiny world that contains a blinker."
  [] ; empty argument vector
  [[0 1 0]
   [0 1 0]
   [0 1 0]])


(defn create-glider []
  [[0 0 0 1 0 0 0 0 0 0 0]
   [0 1 0 1 0 0 0 0 0 0 0]
   [0 0 1 1 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]])


(defn world-width [world]
  (count (nth world 0)))


(defn world-height [world]
  (count world))


(defn element-at
  "Returns the element at the given position.
  If the position is outside the array it will return
  0, marking dead space."
  [world [x y]]
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
    (println (map (fn [cell] (if (zero? cell) " " "+"))
                  row))))

(def ppw pretty-print-world) ; alias for easy access in repl

(defn live
  "This function will apply the given update-fn cycles-times,
  print the steps and return the result"
  [world update-fn cycles]
   (if (zero? cycles)
     ;; true case - just return the world
     world
     ;; false case - update, print and go again
     (let [new-world (update-fn world)] ; build new world
       (println)
       (pretty-print-world new-world) ; print it
       (recur new-world update-fn (dec cycles))))) ; run again until cycles are zero



;; Get going there
;; ----------------------------------------------------------------------------
(defn update-world [world]
  ;; This is your chance to change the world! ;)
  (create-world (world-width  world)
                (world-height world))
  )  ;; return new world

;; To start the GUI call:
;; (start-rendering)
;;
;; To dispay a world call:
;; (display-world (create-blinker))
;;
;; To start a simulation call:
;; (start-simulation (create-blinker))
;;
;; To stop the simulation:
;; (stop-simulation)
;;
;; In case of an exception adjust you update-world function and restart the
;; simulation (no need to restart anything else):
;; (start-simulation (create-blinker))
;;
;; If you prefer simple text, the function ppw and live are your friends :) .
;;
;; Good luck! Have Fun. :)
;;
;;
;; PS:
;;
;; Adjust the simulation speed by calling:
;; (reset! simulation-sleep-ms 1000)












;; ----------------------------------------------------------------------------
; Here be dragons.
;
; Code in here is stuff neede for the fancy GUI repensentation.
; It deals with threads and shared variables. While this is fun and fairly easy
; to write and coordinate in Clojure, it still should be considered a 2nd step.
; Feel free to browse this code, but you don't have to understand any of this
; right now ;).
;; ----------------------------------------------------------------------------

; The shared global state of a world.
(def rendered-w (atom [[1 0]
                       [0 1]]))

; This serves as the thread that runs the endless simulation loop.
(def simulation-running (agent false))
(def simulation-sleep-ms (atom 600))
(def fps (atom 50))

(defn simulate
  "Executes the side effect of changing the rendered-w to the next state
  calculated by update-world.
  Also resends itself to the agent to create a endless loop.
  Can be stopped by setting simulation-running to false.
  Use stop-simulation for this."
  [running]
  (when running
    (try
      (swap! rendered-w update-world) ; side effect! assign new state
      (Thread/sleep @simulation-sleep-ms)
      (send *agent* simulate) ; trigger the loop
      true ; this is the new value for simulation-running
      (catch Exception e
        (println "Caught exception during simulation press <enter> to proceed.")
        (print-stack-trace e)
        (println "To restart the simulation simply call
                 (start-simulation <world_value>) again.")
        false)))) ; in case of an exception stop the loop

(defn start-simulation
  "Starts to simulate the world in a seperate thread.
  Displays the result in the GUI.
  Call stop-simulation to stop it."
  [w]
  (reset! rendered-w w)
  (send simulation-running (constantly true))
  (send simulation-running simulate))

(defn stop-simulation
  "Stops the simulation loop."
  []
  (send simulation-running (constantly false)))

(defn display-world
  "Displays the given world in the GUI.
  Also stops the simulation."
  [w]
  (stop-simulation) ; side effect
  (reset! rendered-w w))


;; Called only once
;; ----------------------------------------------------------------------------
(defn setup []
  (smooth)
  (frame-rate @fps)
  (background 0)) ;; black background

;; Called for each frame
;; ----------------------------------------------------------------------------
(defn draw []
  (stroke 0)
  (stroke-weight 0)
  (let [tile-width (/ (width) (world-width @rendered-w))
        tile-height (/ (height) (world-height @rendered-w))]
    (dorun  ;; force immediate evaluation of lazy sequence
      (for [x (range (world-width @rendered-w))
            y (range (world-height @rendered-w))]
        (do  ;; we do our drawing side-effects here
          ;; set fill based on cell alive state
          (fill (* 255 (element-at @rendered-w [x y])))
          ;; draw a rect for each cell
          (rect (* x tile-width) (* y tile-height)
                tile-width tile-height))))))


;; Main function
;; ----------------------------------------------------------------------------
(defn start-rendering []
  ;; this is quil starting a drawing cycle
  (defsketch example
    :title "Game of Life"
    :setup setup
    :draw draw
    :size [(max 200 (* (world-width  @rendered-w) 15)),
           (max 200 (* (world-height @rendered-w) 15))]))

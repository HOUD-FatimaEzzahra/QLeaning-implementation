# QLearning - Résolution de Maze
Ce dépôt GitHub contient l'implémentation de l'algorithme QLearning pour résoudre un labyrinthe (maze) en utilisant l'apprentissage par renforcement. 

## Apprentissage par renforcement et QLearning
L'apprentissage par renforcement est une branche de l'intelligence artificielle qui s'inspire du comportement d'apprentissage des êtres humains et des animaux. Il repose sur l'idée que l'agent apprend à interagir avec un environnement en recevant des récompenses ou des punitions en fonction de ses actions. L'objectif de l'apprentissage par renforcement est de maximiser la récompense totale que l'agent reçoit au fil du temps.

QLearning est un algorithme d'apprentissage par renforcement qui permet à un agent d'apprendre une fonction d'action-valeur, appelée Q-valeur, qui représente l'utilité prévue de prendre une action donnée dans un état donné. L'algorithme QLearning utilise une approche de programmation dynamique pour mettre à jour les valeurs de Q en fonction des récompenses reçues et des estimations des valeurs futures.

## Labyrinthe (Maze) et principe de fonctionnement

Le labyrinthe est un environnement dans lequel l'agent doit naviguer pour atteindre un objectif. Il est représenté sous forme d'une grille avec des cases et des murs. Chaque case peut avoir des récompenses positives ou négatives associées, et l'agent doit apprendre à trouver le chemin optimal pour maximiser sa récompense totale.

L'algorithme QLearning fonctionne selon les étapes suivantes :

1. **Initialisation** : L'agent initialise une table de valeurs Q qui associe chaque paire (état, action) à une valeur initiale.
2. **Sélection de l'action** : L'agent sélectionne une action à prendre dans l'état actuel en utilisant une politique d'exploration ou d'exploitation.
3. **Exécution de l'action** : L'agent exécute l'action sélectionnée et observe la récompense associée et l'état résultant.
4. **Mise à jour de la valeur Q** : L'agent met à jour la valeur Q de l'état précédent en utilisant la formule de mise à jour de Q-learning.
5. **Répétition** : Les étapes 2 à 4 sont répétées jusqu'à ce que l'agent atteigne l'état final ou un critère d'arrêt défini.
La formule de mise à jour de Q-learning est la suivante :

```bash
  Q(s, a) = Q(s, a) + α * (r + γ * max[Q(s', a')] - Q(s, a))
  //formule ou équation de Bellman
```
où :

- **Q**(s, a) est la valeur Q de l'état s et de l'action a.
- **α** est le taux d'apprentissage (learning rate) qui contrôle l'importance accordée aux nouvelles informations par rapport aux anciennes.
- **r** est la récompense obtenue en exécutant l'action a dans l'état s.
- **γ** est le facteur d'escompte (discount factor) qui pondère l'importance des récompenses futures par rapport aux récompenses immédiates.

## Contenu 
Ce dépôt GitHub contient les implémentations suivantes :
1. [Version standard du QLearning](https://github.com/HOUD-FatimaEzzahra/QLeaning-implementation/tree/main/src/main/java/ma/enset/version_sequentielle)
2. [Version SMA (Multi-Agent) du QLearning](https://github.com/HOUD-FatimaEzzahra/QLeaning-implementation/tree/main/src/main/java/ma/enset/version_sma)


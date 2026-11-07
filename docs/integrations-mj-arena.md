# Types de quetes Runes et OneBlock

Installer les trois JAR fournis (zQuests, Runes, OneBlock), remplacer les anciennes versions et redemarrer le serveur.
Le correctif ENTITY_KILL / oneblock:capybara est conserve.

Le fichier exemples-quetes.yml contient treize quetes completes de demonstration, sans recompenses. Ajouter vos recompenses et adapter les noms/goals avant installation dans plugins/zQuests/quests/.

- COMMAND existe deja : actions[].commands contient les commandes sans slash. La comparaison existante utilise un prefixe ; "runes" accepte donc "runes" et ses sous-commandes. Cela mesure la saisie d'une commande non annulee, pas sa reussite.
- RUNE_CRAFT : actions[].rune accepte l'identifiant d'une rune ultime (tempete, midas, titan) ou "*". Une fusion reussie a la table donne un point. Les dons par commande, apercus de recette, chargements et poses sur armure ne comptent pas.
- ONEBLOCK_LEVEL : goal est le nombre de niveaux gagnes apres activation de la quete, pas le niveau absolu a atteindre.
- ONEBLOCK_PRESTIGE : goal est le nombre de prestiges gagnes apres activation. Un cycle au prestige maximal ne compte pas.
- ONEBLOCK_PHASE : goal est le nombre de phases terminees apres activation. actions[].phase peut filtrer l'identifiant de la phase terminee, ou "*" pour toutes. Les phases des differentes dimensions comptent.
- ONEBLOCK_BLOCK_BREAK : casse du bloc magique d'une ile (n'importe quelle ile, toutes dimensions), par le joueur qui casse. actions[].material ou actions[].tag filtre le bloc casse. Les autres blocs de l'ile ne comptent pas (utiliser BLOCK_BREAK).

Les trois progressions OneBlock creditent les membres connectes de l'ile, comme les recompenses natives. Pas de rattrapage pour les membres deconnectes, ni de credit pour un ajout de blocs administrateur.

Sources modifiees directement dans les trois projets. zQuests compile contre libs/runes-1.0.0.jar et libs/OneBlock-1.0-SNAPSHOT-api.jar ; ces bibliotheques sont compileOnly et ne sont pas embarquees dans son JAR.

## Shops
Le plugin Shops fourni dans ton projet expose deja les evenements utilises : il n'a pas besoin d'etre modifie.
- BUY_SHOP : achats reussis au /shop. goal compte les objets achetes (un lot de 16 donne 16 points). item: "*" ou cle Shops (DIAMOND, nexo:identifiant...).
- SELL_SHOP : ventes reussies au /shop. goal compte les objets vendus (un lot de 16 donne 16 points). item: "*" ou cle Shops.
- ORDER_COMPLETE : orders entierement livres. Le livreur gagne un point par order termine ; le client n'est pas credite. Les livraisons partielles, prises et creations d'orders ne comptent pas. item: "*" ou cle de l'objet demande.
- AH_BUY : achats confirmes au /ah. goal compte les objets du lot achete, pas les annonces. item: "*" ou materiau Bukkit ; le filtre AH ne distingue pas les identifiants Nexo partageant un meme materiau.

Validation : compilation des trois plugins, 13 tests Runes reussis. Suite OneBlock : 151 tests reussis sur 152 ; EffectConfigTest.lectureSansPotion attend mob:momie alors que modules/bonus/config.yml contient mob:bison, mob:alligator et mob:gnome_red. Ces fichiers n'ont pas ete modifies. Les nouveaux objectifs doivent encore etre verifies sur le serveur en jeu.

36 verifications ciblees reussies : filtres, quantites, destinataires, conversions API, commande existante et compatibilite capybara.


## Suites de quetes
Un fichier de quests/ contenant `suite: true` devient une suite : ses quetes sont des etapes jouees dans l'ordre du fichier. Exemple complet : quests/example_tutorial.yml.
La premiere etape demarre automatiquement a la connexion (auto-accept inutile, et ignore sur les etapes). Quand une etape est terminee, ses rewards sont donnees puis l'etape suivante demarre aussitot ; aucun "start quest" a ecrire. Une etape ajoutee plus tard en fin de fichier est donnee a la prochaine connexion aux joueurs qui avaient fini la suite. Ne jamais renommer une etape deja jouee : la progression est sauvegardee par nom.
L'ancien systeme "complex" (example_complex.yml) et l'option sequential-quests ont ete retires.

## Types de quetes : principal, secondaire, journalier
En haut d'un fichier de quests/ : `type: principal`, `type: secondaire` ou `type: journalier` (a ne pas confondre avec le type de chaque quete, BLOCK_BREAK, COMMAND...). Toutes les quetes du fichier prennent ce type. Un fichier sans type n'apparait dans aucun emplacement du HUD.
journalier : chaque jour a 00h00 (heure du serveur), les quetes journalieres en cours ou terminees sont effacees puis redemarrees (auto-accept, ou etape 1 si le fichier est une suite). Une quete pas finie perd sa progression. Un joueur hors ligne a minuit est remis a zero a sa prochaine connexion. Exemple : quests/example_daily.yml.

## Quetes en cours (une par type)
PlaceholderAPI : %zquests_current_text_<N>%, %zquests_current_rewards_count_<N>%, %zquests_current_exists_<N>% avec N = 1 (principal), 2 (secondaire) ou 3 (journalier). Le nom du type marche aussi : %zquests_current_text_journalier%.
Exemple : '&f%zquests_current_text_3% &7(&e%zquests_current_rewards_count_3% recompenses&7)'
Si le joueur a plusieurs quetes en cours du meme type, la plus ancienne est affichee. Sans quete de ce type : current_text_N est vide, current_rewards_count_N vaut 0, current_exists_N vaut false. Les anciens %zquests_current_text%, %zquests_current_rewards_count% et %zquests_current_exists% restent disponibles et correspondent a l'emplacement 1 (principal).

## OB_JOIN
Valide quand le joueur a une ile OneBlock. actions[].role : chef (il est proprietaire de l'ile, donc il l'a creee), membre (il a rejoint l'ile d'un autre) ou "*" pour les deux (par defaut). goal doit valoir 1.
Verification chaque seconde dans le cache OneBlock : un joueur qui avait deja une ile avant le debut de la quete la valide aussi tout de suite. L'ile doit etre sur le serveur ou tourne zQuests. Si le joueur joue sur l'ile de sa guilde, c'est cette ile qui compte (chef seulement s'il en est le proprietaire).
```yaml
  - type: OB_JOIN
    actions:
      - role: "*" # chef, membre ou *
    name: "tutorial-ile"
    display-name: "Ton ile"
    description: "&fCree ton ile ou rejoins celle d'un ami"
    goal: 1
```

## Recompenses de la quete en cours
PlaceholderAPI : %zquests_current_rewards_count_<N>% (ou %zquests_current_rewards_count% pour la position 1)
Exemple : '&eRecompenses : &f%zquests_current_rewards_count_1%'
Compte les entrees de la liste rewards de la quete a la position N. Retourne 0 sans joueur ou sans quete a cette position. Une entree donnant 64 objets compte pour 1 ; une action de message compte aussi pour 1. Les global-rewards et permissible-rewards ne sont pas incluses.

## RUNE_FORGE
Chaque eclair qui ajoute effectivement une charge a une rune sur une enclume donne 1 point au proprietaire connecte. Cela inclut les eclairs du systeme d'orage et les vrais eclairs pris en charge par Runes (dont le baton). Une rune deja pleine ne compte pas ; aucun rattrapage hors ligne. Le dernier eclair qui remplit la rune compte egalement. actions[].rune: "*" accepte toutes les runes, sinon utiliser l'identifiant de runes.yml. goal compte les charges ajoutees, pas les runes entierement chargees. Remplacer Runes et zQuests puis redemarrer.


## GUILD_NUMBER et ECO_NUMBER
GUILD_NUMBER : metric: members (nombre actuel de membres, chef inclus) ou metric: level (niveau actuel de la guilde). Sans guilde, la valeur vaut 0. Les valeurs sont relues dans le cache chaque seconde, notamment apres connexion et changement de quete courante.
ECO_NUMBER : currency: or, diamant ou gemme (ou autre identifiant configure dans Economie). mode: balance vise le solde actuel sans debiter le joueur ; mode: earned cumule les augmentations de solde pendant que la quete peut progresser. Les depenses ne diminuent pas le cumul. Les dons, transferts, recompenses et hausses administrateur comptent aussi. Les gains sont ceux des evenements du serveur courant pour un joueur connecte : aucun rattrapage des gains hors ligne ou effectues sur un autre serveur. Le solde, lui, suit le cache synchronise d'Economie.
Les objectifs sont en plus petite unite monetaire pour conserver les montants exacts. Les monnaies actuelles ont decimals: 0, donc goal: 1000 = 1000 unites. Avec decimals: 2, goal: 1000 = 10,00. goal doit etre compris entre 1 et 2147483647.
Utiliser une seule action par quete pour ces types afin de ne pas melanger membres/niveau ou solde/gains/monnaies. Les objectifs de valeur actuelle peuvent baisser avant validation, puis restent termines une fois valides. Aucun changement des plugins Guilde ou Economie n'est necessaire ; remplacer seulement zQuests.


## Peche (CUSTOM_FISHING et SELL_FISH)
Remplacer Peche et zQuests puis redemarrer. zQuests compile contre libs/Peche-1.0.0.jar (compileOnly, non embarque).
- CUSTOM_FISHING : un point par poisson RunesTales pris (mini-jeu de ferrage reussi). Chaque ligne en plus du multi-lignes compte aussi. Un poisson rate, le butin vanilla et /peche donner ne comptent pas.
- SELL_FISH : poissons vendus avec /peche vendre (ou sell). goal compte les poissons (une pile de 5 donne 5 points), pas l'argent.
Filtres de actions[] (tous facultatifs, cumulables) :
- poisson : identifiant de poissons.yml (vairon_argente, ancetre_des_marees...) ou "*".
- rarete : commun, peu_commun, rare, epique, legendaire, mythique, ou "*".
- palier : 1 a 4, ou 0 pour tous.
```yaml
  - type: CUSTOM_FISHING
    name: pecher-10-rares
    display-name: Pecher 10 poissons rares
    actions:
      - rarete: "rare"
    goal: 10

  - type: SELL_FISH
    name: vendre-20-poissons-abysses
    display-name: Vendre 20 poissons des abysses
    actions:
      - palier: 3
    goal: 20
```

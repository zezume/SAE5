INSERT INTO question (id_question,intitule, description, type)
VALUES (1,'En moyenne, combien d’heures par jour passez-vous sur votre smartphone ?', '', 'radio'),
       (2,'Combien de temps passez-vous chaque jour devant un ordinateur ?', '', 'radio'),
       (3,'Combien d’heures de vidéos en streaming regardez-vous par semaine (Netflix, YouTube, Twitch, etc.) ?', '', 'radio'),
       (4,'Regardez-vous vos vidéos en qualité :', '', 'radio'),
       (5,'À quelle fréquence changez-vous de smartphone ?', '', 'radio'),
       (6,'Combien d’appareils numériques possédez-vous actuellement (ordinateur, smartphone, tablette, console, TV connectée, etc.) ?', '', 'radio'),
       (7,'Stockez-vous principalement vos photos et fichiers :', '', 'radio'),
       (8,'Sauvegardez-vous régulièrement vos mails ou les laissez-vous indéfiniment dans votre boîte ?', '', 'radio'),
       (9,'Avez-vous déjà réparé un appareil électronique au lieu de le remplacer ?', '', 'radio'),
       (10,'Connaissez-vous l’impact énergétique de vos usages numériques par rapport à la moyenne nationale ?', '', 'radio');

INSERT INTO Questionnaire(id_questionnaire,titre)
values (1,'Questionnaire');
INSERT INTO QuestionnaireQuestion(id_questionnaire,id_question,ordre)
values
    (1,1,1),
    (1,2,2),
    (1,3,3),
    (1,4,4),
    (1,5,5),
    (1,6,6),
    (1,7,7),
    (1,8,8),
    (1,9,9),
    (1,10,10);

INSERT INTO reponse (id_question, id_reponse, reponse)
VALUES
    (1, 1, 'Moins de 2h'),
    (1, 2, '2h à 4h'),
    (1, 3, '4h à 6h'),
    (1, 4, 'Plus de 6h'),
    (2, 1, 'Moins d’1h'),
    (2, 2, '1h à 3h'),
    (2, 3, '3h à 5h'),
    (2, 4, 'Plus de 5h'),
    (3, 1, 'Moins de 3h'),
    (3, 2, '3h à 7h'),
    (3, 3, '7h à 15h'),
    (3, 4, 'Plus de 15h'),
    (4, 1, 'Standard (SD)'),
    (4, 2, 'Haute définition (HD)'),
    (4, 3, 'Très haute définition (4K ou plus)'),
    (5, 1, 'Tous les ans'),
    (5, 2, 'Tous les 2 ans'),
    (5, 3, 'Tous les 3 à 4 ans'),
    (5, 4, 'Plus de 4 ans'),
    (6, 1, '1 à 2'),
    (6, 2, '3 à 4'),
    (6, 3, '5 à 6'),
    (6, 4, 'Plus de 6'),
    (7, 1, 'Sur votre appareil (mémoire interne/disque dur)'),
    (7, 2, 'Sur un cloud (Google Drive, iCloud, etc.)'),
    (7, 3, 'Un mélange des deux'),
    (8, 1, 'Oui, je trie et supprime régulièrement'),
    (8, 2, 'Non, je garde tout'),
    (9, 1, 'Oui'),
    (9, 2, 'Non'),
    (10, 1, 'Oui'),
    (10, 2, 'Non');

INSERT INTO question (id_question,intitule, description, type)
VALUES
    (11,'Combien de grammes de CO₂ équivaut un mail de 1 Mo ?', '', 'radio'),
    (12,'Combien de kilomètres en voiture équivaut la durée de vie d''un ordinateur ?', '', 'radio'),
    (13,'Quelle est la cause principale de l’émission de CO₂ d’une tablette ?', '', 'radio'),
    (14,'Écouter la radio pendant 1h émet moins que le streaming sur internet : vrai ou faux ?', '', 'radio'),
    (15,'D’où proviennent 80% des émissions de CO₂ des appareils numériques ?', '', 'radio'),
    (16,'Combien d’années faut-il garder son smartphone pour diviser par deux son empreinte par rapport à 2 ans ?', '', 'radio'),
    (17,'Le cloud n’émet aucun CO₂ : vrai ou faux ?', '', 'radio'),
    (18,'Quel geste simple réduit le plus la consommation énergétique d’un smartphone ?', '', 'radio'),
    (19,'Quel élément consomme le plus d’énergie lors d’une visioconférence ?', '', 'radio'),
    (20,'Est-il plus écologique de réparer un ordinateur plutôt que d’en acheter un neuf ?', '', 'radio');
INSERT INTO Questionnaire(id_questionnaire,titre)
VALUES (2,'Escape Game');
INSERT INTO QuestionnaireQuestion(id_questionnaire,id_question,ordre)
VALUES
    (2,11,1),
    (2,12,2),
    (2,13,3),
    (2,14,4),
    (2,15,5),
    (2,16,6),
    (2,17,7),
    (2,18,8),
    (2,19,9),
    (2,20,10);
INSERT INTO reponse (id_question, id_reponse, reponse,bonne)
VALUES
    -- Q1
    (11, 1, '20 g de CO₂',false),
    (11, 2, '10 g de CO₂',false),
    (11, 3, '3 g de CO₂',true),
    -- Q2
    (12, 1, '800 km',false),
    (12, 2, '1 000 km',false),
    (12, 3, '1 400 km',true),
    -- Q3
    (13, 1, 'Le transport du produit',false),
    (13, 2, 'La fabrication de l’appareil',true),
    (13, 3, 'L’utilisation quotidienne',false),
    -- Q4
    (14, 1, 'Vrai',true),
    (14, 2, 'Faux',false),
    -- Q5
    (15, 1, 'De leur utilisation',false),
    (15, 2, 'De leur fin de vie (recyclage)',false),
    (15, 3, 'De leur fabrication',true),
    -- Q6
    (16, 1, '3 ans',false),
    (16, 2, '4 ans',false),
    (16, 3, '5 ans',true),
    -- Q7
    (17, 1, 'Vrai',false),
    (17, 2, 'Faux',true),
    -- Q8
    (18, 1, 'Réduire la luminosité de l’écran',true),
    (18, 2, 'Désactiver le mode avion',false),
    (18, 3, 'Utiliser un fond d’écran animé',false),
    -- Q9
    (19, 1, 'La caméra vidéo',true),
    (19, 2, 'Le micro',false),
    (19, 3, 'Le chargement de la page web',false),
    -- Q10
    (20, 1, 'Vrai',true),
    (20, 2, 'Faux',false);

INSERT INTO Conseil (titre, description, type) VALUES
                                                   ('Éteindre les appareils au lieu de les laisser en veille', 'Éteindre complètement les appareils lorsqu’ils ne sont pas utilisés pour réduire la consommation énergétique.', 'energie'),
                                                   ('Activer le mode économie d’énergie', 'Activer le mode économie d’énergie sur tous les appareils pour diminuer la consommation électrique.', 'energie'),
                                                   ('Réduire la luminosité des écrans', 'Baisser la luminosité des écrans pour consommer moins d’énergie.', 'energie'),
                                                   ('Débrancher les chargeurs inutilisés', 'Débrancher les chargeurs lorsqu’ils ne sont pas utilisés pour éviter la consommation fantôme.', 'energie'),
                                                   ('Programmer la mise en veille automatique', 'Configurer la mise en veille automatique après quelques minutes d’inactivité pour économiser l’énergie.', 'energie'),
                                                   ('Privilégier le Wi-Fi au réseau mobile', 'Le Wi-Fi consomme moins d’énergie que la 4G/5G pour la même utilisation.', 'energie'),
                                                   ('Regrouper les appareils sur des multiprises à interrupteur', 'Permet d’éteindre facilement plusieurs appareils en même temps.', 'energie'),
                                                   ('Garder ses appareils plus longtemps', 'Prolonger la durée de vie des appareils pour réduire l’empreinte carbone.', 'co2'),
                                                   ('Favoriser la réparation plutôt que le remplacement', 'Réparer les appareils plutôt que de les remplacer pour limiter le CO2 émis.', 'co2'),
                                                   ('Revendre ou donner les appareils inutilisés', 'Donner ou revendre permet de prolonger leur utilisation et réduire le gaspillage.', 'co2'),
                                                   ('Acheter des appareils reconditionnés', 'Préférer les appareils reconditionnés pour réduire l’empreinte carbone de la production.', 'co2'),
                                                   ('Recycler correctement les appareils électroniques', 'Recycler les appareils en fin de vie pour limiter leur impact environnemental.', 'co2'),
                                                   ('Privilégier les appareils labellisés', 'Choisir Energy Star ou EPEAT pour un meilleur rendement énergétique.', 'co2'),
                                                   ('Supprimer les fichiers et applications inutiles', 'Nettoyer régulièrement les fichiers pour réduire la consommation de stockage et d’énergie.', 'bonnes_pratiques'),
                                                   ('Nettoyer les photos et vidéos en doublon', 'Éliminer les doublons pour réduire l’espace utilisé et l’énergie consommée.', 'bonnes_pratiques'),
                                                   ('Vider les corbeilles locales et cloud', 'Supprimer définitivement les fichiers inutiles pour optimiser la consommation.', 'bonnes_pratiques'),
                                                   ('Archiver les données anciennes sur disque externe', 'Limiter l’usage du cloud pour réduire la consommation énergétique.', 'bonnes_pratiques'),
                                                   ('Limiter le streaming en haute définition', 'Préférer 720p plutôt que 1080p ou 4K pour réduire l’énergie consommée.', 'energie'),
                                                   ('Télécharger les contenus plutôt que de les streamer', 'Consomme moins de ressources et de bande passante.', 'energie'),
                                                   ('Désactiver la lecture automatique sur les plateformes vidéo', 'Évite le streaming inutile et économise de l’énergie.', 'energie'),
                                                   ('Bloquer les publicités sur le navigateur', 'Permet de charger les pages plus rapidement et consommer moins d’énergie.', 'energie'),
                                                   ('Fermer les onglets inutilisés', 'Réduit la consommation mémoire et d’énergie.', 'energie'),
                                                   ('Utiliser des moteurs de recherche écologiques', 'Ecosia ou Lilo réduisent l’impact environnemental de la recherche.', 'autre'),
                                                   ('Désinstaller les extensions inutilisées', 'Les extensions consomment de la mémoire et de l’énergie.', 'energie'),
                                                   ('Utiliser des logiciels légers', 'Réduit la consommation des ressources et l’impact énergétique.', 'bonnes_pratiques'),
                                                   ('Éviter les applications tournant en arrière-plan', 'Limiter l’usage inutile de ressources pour économiser l’énergie.', 'energie'),
                                                   ('Mettre à jour régulièrement OS et logiciels', 'Optimise les performances et réduit la consommation.', 'bonnes_pratiques'),
                                                   ('Utiliser des outils collaboratifs optimisés', 'Réduit l’envoi de fichiers lourds et la consommation serveur.', 'bonnes_pratiques'),
                                                   ('Préférer les laptops aux desktops', 'Moins gourmands en énergie pour un usage équivalent.', 'energie'),
                                                   ('Choisir des écrans LED', 'Moins énergivores que les anciens écrans LCD.', 'energie'),
                                                   ('Débrancher les périphériques inactifs', 'Évite la consommation fantôme des périphériques.', 'energie'),
                                                   ('Optimiser la batterie des appareils', 'Éviter de laisser la batterie à 100% ou 0% pour prolonger sa durée de vie.', 'energie'),
                                                   ('Limiter les impressions', 'Imprimer uniquement lorsque nécessaire pour réduire le papier et l’énergie.', 'bonnes_pratiques'),
                                                   ('Imprimer en recto-verso', 'Réduit la consommation de papier et d’énergie.', 'bonnes_pratiques'),
                                                   ('Réutiliser le papier brouillon', 'Diminue le gaspillage et l’impact environnemental.', 'bonnes_pratiques'),
                                                   ('Privilégier les documents numériques', 'Réduit la consommation papier et l’énergie liée aux impressions.', 'bonnes_pratiques'),
                                                   ('Éviter l’envoi d’e-mails volumineux', 'Compresse ou partage via cloud pour réduire l’impact énergétique.', 'energie'),
                                                   ('Supprimer les anciens emails', 'Réduit le stockage et la consommation énergétique des serveurs.', 'energie'),
                                                   ('Limiter les notifications push', 'Évite l’usage constant du réseau et la consommation d’énergie.', 'energie'),
                                                   ('Éteindre le GPS quand non utilisé', 'Réduit la consommation de batterie et d’énergie.', 'energie'),
                                                   ('Optimiser le stockage cloud', 'Supprimer les fichiers inutiles pour réduire l’impact énergétique.', 'energie'),
                                                   ('Utiliser des applications open-source légères', 'Réduit la consommation et encourage la durabilité logicielle.', 'bonnes_pratiques'),
                                                   ('Préférer les vidéoconférences audio quand possible', 'Moins gourmand en bande passante que la vidéo.', 'energie'),
                                                   ('Éviter les téléchargements multiples simultanés', 'Diminue la consommation réseau et d’énergie.', 'energie'),
                                                   ('Nettoyer régulièrement le cache et l’historique', 'Optimise la mémoire et réduit la consommation.', 'bonnes_pratiques'),
                                                   ('Utiliser des périphériques partagés', 'Éviter l’achat d’équipements supplémentaires.', 'co2'),
                                                   ('Limiter l’usage des serveurs personnels', 'Réduit la consommation d’électricité et l’empreinte carbone.', 'co2'),
                                                   ('Privilégier les applications web légères', 'Réduit l’utilisation des ressources locales et serveur.', 'energie'),
                                                   ('Désactiver la synchronisation automatique non essentielle', 'Réduit la consommation réseau et serveur.', 'energie'),
                                                   ('Sensibiliser les collègues et utilisateurs', 'Partager les bonnes pratiques Green IT pour multiplier l’impact positif.', 'autre');

INSERT
INTO    diseases_table(id, uuid, disease_name)
VALUES  (1, 'lCGyU7hKLmLK', 'Gray leaf spot'),
        (2, 'jJGyU7hKLoNK', 'Common rust'),
        (3, 'fDGxU7hKLmGK', 'Northern leaf blight'),
        (4, 'kDGyU7hKLdL3', 'Health');

INSERT
INTO roles_table(id, uuid, name)
VALUES  (1, 'GSH12dsfdrh2', 'ROLE_ADMIN'),
        (2, 'GSH12dsfdRh2', 'ROLE_USER');

INSERT
INTO symptoms_table(id, uuid, symptom_description, disease_id)
VALUES  (1, 'lFTrt78nmkhd', 'small leaf spots with light halos or dead areas', 1),
        (2, 'jFTht78nmkhd', 'Discolourations, empty grains, and lesions on seeds', 1),
        (3, 'hlTht78nmkhd', 'Severe infection may result in the yellowing of the whole leaf', 1),
        (4, 'GFTht88nmkhd', 'Seeds show abnormal colours with nectronic areas', 1),
        (5, 'GFhjf88nmkhd', 'Lesions begin as flecks on leaves that develop into small tan spots', 2),
        (6, 'Glkjf88nMkhd', 'Pustules turn dark brown to black late in the season', 2),
        (7, 'GlkjmnknMkhd', 'Stalks grow weak and soft and are prone to lodging ', 2),
        (8, 'Glkdf88nMkhd', 'Plants infected during early stages show chlorosis of leaves and death', 2),
        (9, 'Glbrf88nMkhd', 'Symptoms occur first on the lower leaves, with long narrow tan lesions parallel to leaf margins with dark margins 25-150mm long', 3),
        (10, 'Glkbg88nMkhd', 'Leaves dry out, wither and die', 3),
        (11, 'hytqf88nMkhd', 'Older spots slowly grow into tan, long cigar shaped nectrotic lesions with distinct dark specks and pale green, water-socked borders', 3),
        (12, 'lkopf88nMkhd', 'Severe yield losses(up to 70%)', 3),
        (13, 'lbhyr1Y6Mkhd', 'During early stages, symptoms appear as small, oval, water-soaked spots on the lower leaved', 3),
        (14, 'tybhjPl6Mkhd', 'Dark green colored plant', 4),
        (15, 'lkoplTghbnhd', 'Firm leaves', 4),
        (16, 'lkoplTdfrcXf', 'Brightly colored flowers', 4),
        (18, 'lkoplTY6hjnm', 'Root system is well developed', 4),
        (19, 'lkopasdfhjnm', 'Well-shaped, good-colored, nutritious grains, pods or fruits', 4);

INSERT
INTO prescriptions_table(id, uuid, disease_prescription, disease_id)
VALUES  (1, 'GhYj89KlBgTd', 'Use available disease-free seeds and resistant varieties for planting', 1),
        (2, 'GhYj00KlBgTd', 'Practice early harvest to reduce the amount of grain mould', 1),
        (3, 'gbYj89KlBgTd', 'Remove and destroy crop residues after each season', 1),
        (4, 'klYj89KlBgTd', 'Plant resistant varieties available locally', 2),
        (5, 'tyYj89KlBgTd', 'Plant early to avoid optimal conditions for infection', 2),
        (6, 'qwYj89KlBgTd', 'Use shorter season varieties that mature early', 2),
        (7, 'opYj89KlBgTd', 'Plan a crop rotation with non-susceptible crops', 2),
        (8, 'weYj89KlBgTd', 'Ensure balanced fertilization with split application of nitrogen', 2),
        (9, 'weYj89K90UTd', 'Grow resistant or tolerant varieties', 3),
        (10, 'weYjklplBgTd', 'Ensure balanced nutrient supply and avoid excessive nitrogen fertilization', 3),
        (11, 'weYfgT7lBgTd', 'Weed regularly in and around the field', 3),
        (12, 'weYjKlolBgTd', 'Rotate with soya beans, or sunflower to avoid extensive spreading', 3),
        (13, 'weYjty6lBgTd', 'Plow deep to bury plant debris and reduce the amount inoculum in the soil', 3),
        (14, 'weYjklo0BgTd', 'Fertilize with right fertilizer mixture and a balanced nutrient supply', 4),
        (15, 'weYjty6lkl07', 'Do not over water the crop during the season', 4),
        (16, 'weYjty6l67jk', 'Do not touch healthy plants after touching infected plants', 4),
        (17, 'weYjty6rt56d', 'Maintain a high number of different varieties of plants around the field', 4),
        (18, 'weYjty6r56hb', 'Remove diseased leaves, fruit of branches at the right time during the growing season', 4),
        (19, 'weYjty6rerv3', 'After harvest, clean up plant debris from the filed or orchard and burn them', 4);



CREATE TABLE `national_operator`
(
    `id`    bigint(20)                NOT NULL AUTO_INCREMENT,
    `name`  varchar(200) charset utf8 NULL,
    `alias` varchar(200) charset utf8 NULL,
    PRIMARY KEY (id)
);

# Use for tenant `al`
# INSERT INTO national_operator (name, alias) VALUES
#     ('Vodafone', 'Orange Mobile'),
#     ('Vodafone', 'Vodafone AL'),
#     ('Vodafone', 'VODAFONE AL'),
#     ('Vodafone', 'Vodafone Albania'),
#     ('One Telecommunications', 'One.al'),
#     ('One Telecommunications', 'Telekom.al'),
#     ('One Telecommunications', 'Albanian Mobile Communications (AMC)'),
#     ('ALBTelecom Mobile', 'Eagle Mobile');
#
#


# Use for tenant `no`
# INSERT INTO national_operator (name, alias) VALUES
#     ('Telenor', 'Telenor Norge AS'),
#     ('Telenor', 'Telenor Norge'),
#     ('Telia', 'Telia Norge AS'),
#     ('Telia', 'Telia Norge EG'),
#     ('Ice', 'Ice');
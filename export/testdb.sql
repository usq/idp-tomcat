-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Oct 17, 2016 at 09:13 PM
-- Server version: 5.5.42
-- PHP Version: 5.6.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `testdb`
--
CREATE DATABASE IF NOT EXISTS `testdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `testdb`;

-- --------------------------------------------------------

--
-- Table structure for table `datatable`
--

DROP TABLE IF EXISTS `datatable`;
CREATE TABLE `datatable` (
  `id` int(10) unsigned NOT NULL,
  `content` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `testtable`
--

DROP TABLE IF EXISTS `testtable`;
CREATE TABLE `testtable` (
  `id` int(10) unsigned NOT NULL,
  `formtext` text
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `testtable`
--

INSERT INTO `testtable` (`id`, `formtext`) VALUES
(1, '{\r\n    "element_type": "form",\r\n    "metadata": {\r\n        "form_id": "1",\r\n        "title": "Sonnen- und Lichtaussetzung",\r\n        "version": "1.0",\r\n        "creation_date": "13.07.2015",\r\n        "creator": "Florian Kohlmayer",\r\n        "is_template": false,\r\n        "category": "Cardiac"\r\n    },\r\n    "children": [\r\n        {\r\n            "description_type": "text",\r\n            "element_id": "101",\r\n            "element_type": "description",\r\n            "text": "Wählen Sie eine Antwortmöglichkeit für jeden unten angegebenen Zeitraum bis zu Ihrem jetzigen Alter aus. Versuchen Sie, einen Durchschnittswert anzugeben."\r\n        },\r\n        {\r\n            "description_type": "text",\r\n            "element_id": "102",\r\n            "element_type": "description",\r\n            "text": "Im Sommer: Wie viel Zeit pro Tag haben Sie durchschnittlich im Freien verbracht (z.B. Spiele, Sport, Spaziergänge, Gartenarbeit, Arbeit)?"\r\n        },\r\n        {\r\n            "element_id": "11",\r\n            "element_type": "interactive",\r\n            "interactive_type": "radio",\r\n            "interactive_details": {\r\n                "label": "0-9 Jahre",\r\n                "options": [\r\n                    {\r\n                        "element_id": "111",\r\n                        "label": "Gar keine"\r\n                    },\r\n                    {\r\n                        "element_id": "112",\r\n                        "label": "Einige Stunden pro Monat"\r\n                    },\r\n                    {\r\n                        "element_id": "113",\r\n                        "label": "Einige Stunden pro Woche"\r\n                    },\r\n                    {\r\n                        "element_id": "114",\r\n                        "label": "Einige Stunden pro Tag"\r\n                    }\r\n                ]\r\n            },\r\n            "mapping_key": "k_radio1",\r\n            "validators": []\r\n        },\r\n        {\r\n            "element_id": "12",\r\n            "element_type": "interactive",\r\n            "interactive_type": "radio",\r\n            "interactive_details": {\r\n                "label": "10-19 Jahre",\r\n                "options": [\r\n                    {\r\n                        "element_id": "121",\r\n                        "label": "Gar keine"\r\n                    },\r\n                    {\r\n                        "element_id": "122",\r\n                        "label": "Einige Stunden pro Monat"\r\n                    },\r\n                    {\r\n                        "element_id": "123",\r\n                        "label": "Einige Stunden pro Woche"\r\n                    },\r\n                    {\r\n                        "element_id": "124",\r\n                        "label": "Einige Stunden pro Tag"\r\n                    }\r\n                ]\r\n            },\r\n            "mapping_key": "k_radio2",\r\n            "validators": []\r\n        },\r\n        {\r\n            "element_id": "13",\r\n            "element_type": "interactive",\r\n            "interactive_type": "radio",\r\n            "interactive_details": {\r\n                "label": "20+ Jahre",\r\n                "options": [\r\n                    {\r\n                        "element_id": "131",\r\n                        "label": "Gar keine"\r\n                    },\r\n                    {\r\n                        "element_id": "132",\r\n                        "label": "Einige Stunden pro Monat"\r\n                    },\r\n                    {\r\n                        "element_id": "133",\r\n                        "label": "Einige Stunden pro Woche"\r\n                    },\r\n                    {\r\n                        "element_id": "134",\r\n                        "label": "Einige Stunden pro Tag"\r\n                    }\r\n                ]\r\n            },\r\n            "mapping_key": "k_radio3",\r\n            "validators": []\r\n        }\r\n    ]\r\n}');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `datatable`
--
ALTER TABLE `datatable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `testtable`
--
ALTER TABLE `testtable`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `datatable`
--
ALTER TABLE `datatable`
  MODIFY `id` int(10) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `testtable`
--
ALTER TABLE `testtable`
  MODIFY `id` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
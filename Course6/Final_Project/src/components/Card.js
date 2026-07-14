import { Heading, HStack, Image, Text, VStack } from "@chakra-ui/react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import React from "react";

const Card = ({ title, description, imageSrc }) => {
  return (
    <VStack
      color="black"
      backgroundColor="white"
      borderRadius="xl"
      overflow="hidden"
      align="start"
      spacing={0}
      cursor="pointer"
    >
      <Image src={imageSrc} alt={title} width="100%" />
      <VStack spacing={4} p={5} align="start">
        <Heading as="h3" size="md">
          {title}
        </Heading>
        <Text color="gray.600" fontSize="lg">
          {description}
        </Text>
        <HStack spacing={2} alignItems="center">
          <Text fontWeight="bold" fontSize="sm">
            See more
          </Text>
          <FontAwesomeIcon icon={faArrowRight} size="1x" />
        </HStack>
      </VStack>
    </VStack>
  );
};

export default Card;

<?xml version="1.0" encoding="ISO-8859-1"?>

<dummy>
  <dummy-tag dummy="name"/>
  <dummy-tag dummy="="/>
  <dummy-tag dummy="name-test-value"/>
  <dummy-tag dummy="value"/>
  <dummy-tag dummy="="/>
  <dummy-tag dummy="${props.value}"/>
  <dummy-tag dummy="extra"/>
  <dummy-tag dummy="="/>
  <dummy-tag dummy="${props.unknown}"/>
  <dummy-map extra="${props.unknown}" name="name-test-value" value="${props.value}"/>
</dummy>

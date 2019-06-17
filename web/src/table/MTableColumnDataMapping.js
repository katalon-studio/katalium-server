import React from 'react';

export default class MTableColumnDataMapping {
  constructor(
    headerCaption,
    fieldName,
    decorate = (name, row) => <div className="text-wrap">{row[name]}</div>,
    isToolbar = false,
  ) {
    this.headerCaption = headerCaption;
    this.fieldName = fieldName;
    this.decorate = decorate;
    this.isToolbar = isToolbar;
  }
}

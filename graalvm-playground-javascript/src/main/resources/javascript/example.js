// const { React, ReactDOMServer } = self;
import React from 'react';
import ReactDOMServer from 'react-dom/server';

(str) => {
    class Hello extends React.Component {
        render() {
            return React.createElement('div', null, `Hello ${this.props.toWhat}`);
        }
    }

    const elem = React.createElement(Hello, {toWhat: 'World'}, null)

    return ReactDOMServer.renderToStaticMarkup(elem);
}

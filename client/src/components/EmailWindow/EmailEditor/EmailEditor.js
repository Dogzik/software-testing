import '../EmailWindow.css';
import './EmailEditor.css';
import React from 'react';

class EmailEditor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      address: '',
      subject: '',
      text: '',
    };
    this.onAddressChange = this.onAddressChange.bind(this);
    this.onSubjectChange = this.onSubjectChange.bind(this);
    this.onTextChange = this.onTextChange.bind(this);
    this.onSend = this.onSend.bind(this);
  }

  onAddressChange(e) {
    this.setState({ address: e.target.value });
  }

  onSubjectChange(e) {
    this.setState({ subject: e.target.value });
  }

  onTextChange(e) {
    this.setState({ text: e.target.value });
  }

  async onSend() {
    const { from, onSend, postClient } = this.props;
    const { address, subject, text } = this.state;
    const newId = await postClient.sendEmail(from, address, subject, text);
    if (newId !== null) {
      await onSend();
    }
  }

  render() {
    const { address, subject, text } = this.state;
    return (
      <div className={'EmailWindow'}>
        <div className={'EmailEditor__info'}>
          <input type={'email'} placeholder={'To'} value={address} onChange={this.onAddressChange}/>
        </div>
        <div className={'EmailEditor__info'}>
          <input type={'text'} placeholder={'Subject'} value={subject} onChange={this.onSubjectChange}/>
        </div>
        <div className={'EmailEditor__text'}>
          <textarea className={'EmailEditor__text'} value={text} onChange={this.onTextChange}/>
          <button className={'EmailEditor__send'} onClick={this.onSend}>Send</button>
        </div>
      </div>
    );
  }
}

export default EmailEditor;

/**获取string,为null则返回''
 * @param s
 * @return
 */
function getString(s) {
  return s == null ? '' : s;
}

/**获取去掉前后空格后的string,为null则返回''
 * @param s
 * @return
 */
function getTrimmedString(s) {
  return this.getString(s).trim();
}

/**获取去掉所有空格后的string,为null则返回''
 * @param s
 * @return
 */
function getNoBlankString(s) {
  return this.getString(s).replace('\\s', '');
}

/**判断字符是否为空
 * @param s
 * @param trim
 * @return
 */
function isEmpty(s, trim) {
  if (s == null) {
    return true;
  }
  if (trim) {
    s = s.trim();
  }
  if (s == '') {
    return true;
  }

  return false;
}


/**添加后缀
 * @param key
 * @param suffix
 * @return key + suffix，第一个字母小写
 */
function addSuffix(key, suffix) {
  key = this.getNoBlankString(key);
  if (key == '') {
    return this.firstCase(suffix);
  }
  return this.firstCase(key) + this.firstCase(suffix, true);
}
/**首字母大写或小写
 * @param key
 * @param upper
 * @return
 */
function firstCase(key, upper) {
  key = this.getString(key);
  if (key == '') {
    return '';
  }

  const first = key.substring(0, 1);
  key = (upper ? first.toUpperCase() : first.toLowerCase()) + key.substring(1, key.length);

  return key;
}

/**全部大写
 * @param s
 * @param trim
 * @return
 */
function toUpperCase(s, trim) {
  s = trim ? this.getTrimmedString(s) : this.getString(s);
  return s.toUpperCase();
}
/**全部小写
 * @param s
 * @return
 */
function toLowerCase(s, trim) {
  s = trim ? this.getTrimmedString(s) : this.getString(s);
  return s.toLowerCase();
}

//校正（自动补全等）字符串>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
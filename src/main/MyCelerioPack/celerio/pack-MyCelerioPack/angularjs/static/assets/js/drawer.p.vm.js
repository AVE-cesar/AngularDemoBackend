$output.resource("static\assets\js", "drawer.js")##

/* ========================================================================
 * Bootstrap: drawer.js v1.0.6
 # Heavily based on collapse, but had to change the name to "fold" to 
 # avoid transition conflicts.
 * ========================================================================
*/


(function (${dollar}) {
  'use strict';

  // OFF CANVAS PUBLIC CLASS DEFINITION
  // ================================

  var Drawer = function (element, options) {
    this.${dollar}element      = ${dollar}(element)
    this.options       = ${dollar}.extend({}, Drawer.DEFAULTS, options)
    this.${dollar}trigger      = ${dollar}(this.options.trigger).filter('[href="#' + element.id + '"], [data-target="#' + element.id + '"]')
    this.transitioning = null

    if (this.options.parent) {
      this.${dollar}parent = this.getParent()
    } else {
      this.addAriaAndDrawerdClass(this.${dollar}element, this.${dollar}trigger)
    }

    if (this.options.toggle) this.toggle()
  }

  Drawer.VERSION  = '3.3.2'

  Drawer.TRANSITION_DURATION = 350

  Drawer.DEFAULTS = {
    toggle: true,
    trigger: '[data-toggle="drawer"]'
  }

  Drawer.prototype.dimension = function () {
    var isRight = this.${dollar}element.hasClass('drawer-right')
    return isRight ? 'margin-right' : 'margin-left'
  }

  Drawer.prototype.show = function () {
    if (this.transitioning || this.${dollar}element.hasClass('open')) return

    var activesData
    var actives = this.${dollar}parent && this.${dollar}parent.children('.panel').children('.in, .collapsing')

    if (actives && actives.length) {
      activesData = actives.data('bs.drawer')
      if (activesData && activesData.transitioning) return
    }

    var startEvent = ${dollar}.Event('show.bs.drawer')
    this.${dollar}element.trigger(startEvent)
    if (startEvent.isDefaultPrevented()) return

    if (actives && actives.length) {
      Plugin.call(actives, 'hide')
      activesData || actives.data('bs.drawer', null)
    }

    var dimension = this.dimension()

    this.${dollar}element
      .addClass('folding').css(dimension, 0)
      .attr('aria-expanded', true)

    this.${dollar}trigger
      .removeClass('folded')
      .attr('aria-expanded', true)

    this.transitioning = 1

    var complete = function () {
      this.${dollar}element
        .removeClass('folding')
        .addClass('fold open').css(dimension, '')
      this.transitioning = 0
      this.${dollar}element
        .trigger('shown.bs.drawer')
    }

    if (!${dollar}.support.transition) {
        return complete.call(this)
    } else {
        this.transEventName = ${dollar}.support.transition.end;
    }

    this.${dollar}element
      .one(this.transEventName, ${dollar}.proxy(complete, this))
      .emulateTransitionEnd(Drawer.TRANSITION_DURATION).css(dimension, 0)
  }

  Drawer.prototype.hide = function () {
    if (this.transitioning || !this.${dollar}element.hasClass('open')) return

    var startEvent = ${dollar}.Event('hide.bs.drawer')
    this.${dollar}element.trigger(startEvent)
    if (startEvent.isDefaultPrevented()) return

    var dimension = this.dimension()

    this.${dollar}element
      .addClass('folding')
      .removeClass('open')
      .attr('aria-expanded', false)

    this.${dollar}trigger
      .addClass('folded')
      .attr('aria-expanded', false)

    this.transitioning = 1

    var complete = function () {
      this.transitioning = 0
      this.${dollar}element
        .removeClass('folding')
        .addClass('fold')
        .trigger('hidden.bs.drawer')
    }

    if (!${dollar}.support.transition) {
        return complete.call(this)
    } else {
        this.transEventName = ${dollar}.support.transition.end;
    }

    this.${dollar}element
      .css(dimension, '')
      .one(this.transEventName, ${dollar}.proxy(complete, this))
      .emulateTransitionEnd(Drawer.TRANSITION_DURATION)
  }

  Drawer.prototype.toggle = function () {
    this[this.${dollar}element.hasClass('open') ? 'hide' : 'show']()
  }

  Drawer.prototype.getParent = function () {
    return ${dollar}(this.options.parent)
      .find('[data-toggle="drawer"][data-parent="' + this.options.parent + '"]')
      .each(${dollar}.proxy(function (i, element) {
        var ${dollar}element = ${dollar}(element)
        this.addAriaAndDrawerdClass(getTargetFromTrigger(${dollar}element), ${dollar}element)
      }, this))
      .end()
  }

  Drawer.prototype.addAriaAndDrawerdClass = function (${dollar}element, ${dollar}trigger) {
    var isOpen = ${dollar}element.hasClass('open')

    ${dollar}element.attr('aria-expanded', isOpen)
    ${dollar}trigger
      .toggleClass('folded', !isOpen)
      .attr('aria-expanded', isOpen)
  }

  function getTargetFromTrigger(${dollar}trigger) {
    var href
    var target = ${dollar}trigger.attr('data-target')
      || (href = ${dollar}trigger.attr('href')) && href.replace(/.*(?=#[^\s]+${dollar})/, '') // strip for ie7

    return ${dollar}(target)
  }


  // OFFCANVAS PLUGIN DEFINITION
  // ==========================

  function Plugin(option) {
    return this.each(function () {
      var ${dollar}this   = ${dollar}(this)
      var data    = ${dollar}this.data('bs.drawer')
      var options = ${dollar}.extend({}, Drawer.DEFAULTS, ${dollar}this.data(), typeof option == 'object' && option)

      if (!data && options.toggle && option == 'show') options.toggle = false
      if (!data) ${dollar}this.data('bs.drawer', (data = new Drawer(this, options)))
      if (typeof option == 'string') data[option]()
    })
  }

  var old = ${dollar}.fn.fold

  ${dollar}.fn.drawer             = Plugin
  ${dollar}.fn.drawer.Constructor = Drawer


  // OFFCANVAS NO CONFLICT
  // ====================

  ${dollar}.fn.drawer.noConflict = function () {
    ${dollar}.fn.fold = old
    return this
  }


  // OFFCANVAS DATA-API
  // =================

  ${dollar}(document).on('click.bs.drawer.data-api', '[data-toggle="drawer"]', function (e) {
    var ${dollar}this   = ${dollar}(this)

    if (!${dollar}this.attr('data-target')) e.preventDefault()

    var ${dollar}target = getTargetFromTrigger(${dollar}this)
    var data    = ${dollar}target.data('bs.drawer')
    var option  = data ? 'toggle' : ${dollar}.extend({}, ${dollar}this.data(), { trigger: this })

    Plugin.call(${dollar}target, option)
  })

})(window.jQuery || {});

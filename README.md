# base16-builder-clojure

A builder of [base16](https://github.com/chriskempson/base16) templates.

## Usage

### Building all templates
    $ lein run update
    $ lein run build

### Building a single template
    $ lein run build --template-name [NAME] --template-dir [DIRECTORY]

## Additional Features
### Floating Point RGB
In addition to providing `base00-rgb-r`, `base00-rgb-g` and `base00-rgb-b`,
which range from 0-255, this builder provides `base00-rgbf-r`, `base00-rgbf-g`
and `base00-rgbf-b`, ranging from 0-1.

### Partials
For templates that have complex colour formats, partials can help to simplify
this repetition.

In order to avoid needing to create a partial for each colour, colours can be used
as sections, bringing the correct colour into scope for the partial. For example,
a `json-rgb` partial:

```mustache
{
  "r": {{rgb-r}},
  "g": {{rgb-g}},
  "b": {{rgb-b}}
}
```

can be used as follows:

```mustache
{{#base00}}{{>json-rgb}}{{/base00}}
```

Note that individual colours are not specified in the partial.

Inside partials, the following keys are available:
* `hex`
* `hex-r`
* `hex-g`
* `hex-b`
* `rgb-r`
* `rgb-g`
* `rgb-b`
* `rgbf-r`
* `rgbf-g`
* `rgbf-b`

Partial files can be placed in the `templates` directory of a template.
Partial names must be listed under the partials key of the template's `config.yaml` file.
